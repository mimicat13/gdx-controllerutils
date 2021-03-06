package de.golfgl.gdx.controllers;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * ControllerMenuDialog extends libGDX' {@link Dialog}. Use {@link #addFocusableActor(Actor)} to safely add
 * Actors to the Stage's focusable actors list. If you use {@link #button(Button, Object)}, it will be called
 * automatically.
 * <p>
 * Created by Benjamin Schulte on 04.11.2017.
 */

public class ControllerMenuDialog extends Dialog {
    protected Array<Actor> buttonsToAdd = new Array<>();
    protected Actor previousFocusedActor;
    protected Actor previousEscapeActor;

    public ControllerMenuDialog(String title, Skin skin) {
        super(title, skin);
    }

    public ControllerMenuDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public ControllerMenuDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
    }

    @Override
    public Dialog button(Button button, Object object) {
        addFocusableActor(button);
        if (getStage() != null && getStage() instanceof ControllerMenuStage)
            ((ControllerMenuStage) getStage()).addFocusableActor(button);
        return super.button(button, object);
    }

    @Override
    protected void setStage(Stage stage) {
        if (stage == null && getStage() != null && getStage() instanceof ControllerMenuStage) {
            ((ControllerMenuStage) getStage()).removeFocusableActors(buttonsToAdd);
        }

        super.setStage(stage);

        if (stage != null && stage instanceof ControllerMenuStage) {
            ((ControllerMenuStage) stage).addFocusableActors(buttonsToAdd);
        }
    }

    @Override
    public Dialog show(Stage stage, Action action) {
        previousFocusedActor = null;
        previousEscapeActor = null;

        super.show(stage, action);

        if (stage instanceof ControllerMenuStage) {
            previousFocusedActor = ((ControllerMenuStage) stage).getFocusedActor();
            previousEscapeActor = ((ControllerMenuStage) stage).getEscapeActor();

            ((ControllerMenuStage) stage).setFocusedActor(getConfiguredDefaultActor());
            ((ControllerMenuStage) stage).setEscapeActor(getConfiguredEscapeActor());
        }

        return this;

    }

    /**
     * @return Actor that should get the focus when the dialog is shown
     */
    protected Actor getConfiguredDefaultActor() {
        return buttonsToAdd.size >= 1 ? buttonsToAdd.get(0) : null;
    }

    /**
     * @return Actor that should take Action when the escape button is hit while the dialog is shown
     */
    protected Actor getConfiguredEscapeActor() {
        return buttonsToAdd.size == 1 ? buttonsToAdd.get(0) : null;
    }

    @Override
    public void hide(Action action) {
        if (getStage() != null && getStage() instanceof ControllerMenuStage) {
            Actor currentFocusedActor = ((ControllerMenuStage) getStage()).getFocusedActor();
            if (previousFocusedActor != null && previousFocusedActor.getStage() == getStage()
                    && (currentFocusedActor == null || !currentFocusedActor.hasParent() ||
                    currentFocusedActor.isDescendantOf(this)))
                ((ControllerMenuStage) getStage()).setFocusedActor(previousFocusedActor);
            Actor currentEscapeActor = ((ControllerMenuStage) getStage()).getEscapeActor();
            if (previousEscapeActor != null && previousEscapeActor.getStage() == getStage()
                    && (currentEscapeActor == null || !currentEscapeActor.hasParent() ||
                    currentEscapeActor.isDescendantOf(this)))
                ((ControllerMenuStage) getStage()).setEscapeActor(previousEscapeActor);
        }

        super.hide(action);
    }

    /**
     * Call this for every actor that can be focused by keys. The actors will be added to the Stage's focusable
     * actors when the dialog is added to a Stage, and removed from Stage's focusable Actors when the dialog is removed
     * from the stage.
     *
     * @param actor
     */
    public void addFocusableActor(Actor actor) {
        buttonsToAdd.add(actor);

        if (getStage() != null && getStage() instanceof ControllerMenuStage)
            ((ControllerMenuStage) getStage()).addFocusableActor(actor);
    }
}
