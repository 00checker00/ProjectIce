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


public class TextureRegionHolderEditor extends HolderEditor<TextureRegion> {
    @Override
    protected void onEdit() {
        EditTextureRegionDialog.showDialog(getStage(), "Select texture", new EditTextureRegionDialog.Listener() {
            @Override
            public void finished(String textureRegion) {
                Assets.Holder<TextureRegion> newHolder = Assets.findRegion(textureRegion);
                value.data = newHolder.data;
                value.name = newHolder.name;
            }
        }, value);
    }

    private static class EditTextureRegionDialog extends VisWindow {
        private Listener listener;
        private VisTextField textureRegion;
        private VisTextButton okButton;
        private VisTextButton cancelButton;

        public EditTextureRegionDialog(String title, Listener listener, Assets.Holder<TextureRegion> value) {
            super(title);
            this.listener = listener;

            TableUtils.setSpacingDefaults(this);
            setModal(true);

            addCloseButton();
            closeOnEscape();

            VisTable buttonsTable = new VisTable(true);
            buttonsTable.add(cancelButton = new VisTextButton(get(EditTextureRegionDialog.Text.CANCEL)));
            buttonsTable.add(okButton = new VisTextButton(get(EditTextureRegionDialog.Text.OK)));

            VisTable fieldTable = new VisTable(true);

            textureRegion = new VisTextField(value.name);
            fieldTable.add(new VisLabel("Texture Region: "));
            fieldTable.add(textureRegion).expand().fill().row();

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
                        listener.finished(textureRegion.getText());
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
                            listener.finished(textureRegion.getText());
                            fadeOut();
                        } catch (NumberFormatException ignore) {
                        }
                    }

                    return super.keyDown(event, keycode);
                }
            };
            textureRegion.addListener(enterListener);
        }


        private static String get(EditTextureRegionDialog.Text text) {
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

        public static void showDialog(Stage stage, String title, Listener listener, Assets.Holder<TextureRegion> value) {
            EditTextureRegionDialog dialog = new EditTextureRegionDialog(title, listener, value);
            stage.addActor(dialog.fadeIn());
        }

        public interface Listener {
            void finished(String textureRegion);
        }
    }
}
