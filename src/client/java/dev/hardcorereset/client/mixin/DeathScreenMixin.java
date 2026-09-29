package dev.hardcorereset.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Pantalla de muerte con 3 opciones:
 *  - Espectar / Respawnear (vanilla, el primer botón)
 *  - Menú principal (vanilla, el segundo botón)
 *  - Respawnear en mundo nuevo (este mod): sale al menú y abre "Crear mundo nuevo".
 */
@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {

	protected DeathScreenMixin(Component title) {
		super(title);
	}

	// Sale del mundo actual (guarda y vuelve al menú principal), igual que el botón vanilla.
	@Shadow
	protected abstract void exitToTitleScreen();

	@Inject(method = "init", at = @At("TAIL"))
	private void hardcorereset$addNewWorldButton(CallbackInfo ci) {
		// Solo en singleplayer.
		if (this.minecraft == null || this.minecraft.getSingleplayerServer() == null) {
			return;
		}

		this.addRenderableWidget(
			Button.builder(Component.literal("Respawnear (mundo nuevo)"), button -> {
				Minecraft mc = Minecraft.getInstance();
				this.exitToTitleScreen();
				hardcorereset$openCreateWorld(mc);
			}).bounds(this.width / 2 - 100, this.height / 4 + 120, 200, 20).build()
		);
	}

	// Se usa reflexión para no depender de la firma exacta de openFresh, que cambia entre versiones.
	private static void hardcorereset$openCreateWorld(Minecraft mc) {
		Runnable backToTitle = () -> mc.setScreen(new TitleScreen());
		try {
			for (Method m : CreateWorldScreen.class.getDeclaredMethods()) {
				if (!m.getName().equals("openFresh") || !Modifier.isStatic(m.getModifiers())) {
					continue;
				}
				Class<?>[] p = m.getParameterTypes();
				if (p.length == 2 && p[0] == Minecraft.class) {
					m.setAccessible(true);
					Object arg = p[1] == Runnable.class ? backToTitle : new TitleScreen();
					m.invoke(null, mc, arg);
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Si algo falla, al menos se queda en el menú principal.
		mc.setScreen(new TitleScreen());
	}
}
