package de.project.ice.editor.editors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.InputValidator;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.i18n.BundleText;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.*;
import de.project.ice.utils.Assets;


public class AnimationHolderEditor extends HolderEditor<Animation> {
    @Override
    protected void onEdit() {
        EditAnimationDialog.showDialog(getStage(), "Edit animation", new EditAnimationDialog.Listener() {
            @Override
            public void finished(String textureRegion, Animation.PlayMode playMode, float frameTime) {
                Assets.Holder<Animation> newHolder = Assets.createAnimation(textureRegion, frameTime, playMode);
                setHolderData(newHolder.data);
                setHolderName(newHolder.name);
            }
        }, value);
    }

    public static class EditAnimationDialog extends VisWindow {
        private Listener listener;
        private VisTextField textureRegion;
        private VisTextField frameTime;
        private VisSelectBox<Animation.PlayMode> playMode;
        private VisTextButton okButton;
        private VisTextButton cancelButton;

        public EditAnimationDialog(String title, Listener listener, Assets.Holder<Animation> holder) {
            super(title);
            this.listener = listener;

            TableUtils.setSpacingDefaults(this);
            setModal(true);

            addCloseButton();
            closeOnEscape();

            VisTable buttonsTable = new VisTable(true);
            buttonsTable.add(cancelButton = new VisTextButton(get(EditAnimationDialog.Text.CANCEL)));
            buttonsTable.add(okButton = new VisTextButton(get(EditAnimationDialog.Text.OK)));

            VisTable fieldTable = new VisTable(true);

            textureRegion = new VisTextField(holder.name);
            fieldTable.add(new VisLabel("Texture Region: "));
            fieldTable.add(textureRegion).expand().fill().row();

            frameTime = new VisValidableTextField(new Validators.FloatValidator());
            if (holder.data != null)
                frameTime.setText(String.valueOf(holder.data.getFrameDuration()));
            fieldTable.add(new VisLabel("Frame Time: "));
            fieldTable.add(frameTime).expand().fill().row();

            playMode = new VisSelectBox<Animation.PlayMode>();
            playMode.setItems(Animation.PlayMode.values());
            if (holder.data != null)
                playMode.setSelected(holder.data.getPlayMode());
            fieldTable.add(new VisLabel("Mode: "));
            fieldTable.add(playMode).expand().fill();

            add(fieldTable).padTop(3).spaceBottom(4);
            row();
            add(buttonsTable).padBottom(3);

            addListeners();

            pack();
            centerWindow();

            textureRegion.focusField();
        }


        private void addListeners() {
            okButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    try {
                        listener.finished(textureRegion.getText(), playMode.getSelected(), Float.parseFloat(frameTime.getText()));
                        fadeOut();
                    } catch (NumberFormatException ignore) {}
                }
            });

            cancelButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    close();
                }
            });

            InputListener enterListener = new InputListener() {
                @Override
                public boolean keyDown(InputEvent event, int keycode) {
                    if (keycode == Input.Keys.ENTER && !okButton.isDisabled()) {
                        try {
                            listener.finished(textureRegion.getText(), playMode.getSelected(), Float.parseFloat(textureRegion.getText()));
                            fadeOut();
                        } catch (NumberFormatException ignore) {
                        }
                    }

                    return super.keyDown(event, keycode);
                }
            };
            textureRegion.addListener(enterListener);
            frameTime.addListener(enterListener);
        }


        private static String get(EditAnimationDialog.Text text) {
            return VisUI.getDialogUtilsBundle().get(text.getName());
        }

        private enum Text implements BundleText {
            CANCEL {
                public String getName() {
                    return "cancel";
                }
            },
            OK {
                public String getName() {
                    return "ok";
                }
            };

            @Override
            public String get() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String format() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String format(Object... arguments) {
                throw new UnsupportedOperationException();
            }
        }

        public static void showDialog(Stage stage, String title, Listener listener, Assets.Holder<Animation> holder) {
            EditAnimationDialog dialog = new EditAnimationDialog(title, listener, holder);
            stage.addActor(dialog.fadeIn());
        }

        public interface Listener {
            void finished(String textureRegion, Animation.PlayMode playMode, float frameTime);
        }
    }
}
