package de.project.ice.editor.editors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.i18n.BundleText;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import de.project.ice.editor.TextureList;
import de.project.ice.utils.Assets;


public class TextureRegionHolderEditor extends HolderEditor<TextureRegion>
{
    @Override
    protected void onEdit()
    {
        EditTextureRegionDialog.showDialog(getStage(), "Select texture", new EditTextureRegionDialog.Listener()
        {
            @Override
            public void ok(String textureRegion)
            {
                Assets.Holder<TextureRegion> newHolder = Assets.findRegion(textureRegion);
                setHolderName(newHolder.name);
                setHolderData(newHolder.data);
            }

            @Override
            public void changed(String textureRegion)
            {
                Assets.Holder<TextureRegion> newHolder = Assets.findRegion(textureRegion);
                setHolderName(newHolder.name);
                setHolderData(newHolder.data);
            }

            @Override
            public void cancel(String originalRegion)
            {
                Assets.Holder<TextureRegion> newHolder = Assets.findRegion(originalRegion);
                setHolderName(newHolder.name);
                setHolderData(newHolder.data);
            }
        }, value);
    }

    private static class EditTextureRegionDialog extends VisWindow
    {
        private Listener listener;
        private VisTextField textureRegion;
        private VisTextButton okButton;
        private VisTextButton cancelButton;
        private final TextureList textureList;
        private String backup;

        public EditTextureRegionDialog(String title, Listener listener, Assets.Holder<TextureRegion> holder)
        {
            super(title);
            this.listener = listener;
            this.backup = holder.name;

            TableUtils.setSpacingDefaults(this);
            setModal(true);

            addCloseButton();
            closeOnEscape();

            VisTable buttonsTable = new VisTable(true);
            buttonsTable.add(cancelButton = new VisTextButton(get(EditTextureRegionDialog.Text.CANCEL)));
            buttonsTable.add(okButton = new VisTextButton(get(EditTextureRegionDialog.Text.OK)));

            VisTable fieldTable = new VisTable(true);

            textureRegion = new VisTextField(holder.name);
            fieldTable.add(new VisLabel("Texture Region: "));
            fieldTable.add(textureRegion).expandX().fill().row();

            textureList = new TextureList();
            textureList.setWidth(Float.MAX_VALUE);

            VisScrollPane scrollPane = new VisScrollPane(textureList);

            fieldTable.add(scrollPane).maxHeight(200).colspan(2).expandX().fill().row();

            add(fieldTable).padTop(3).spaceBottom(4);
            row();
            add(buttonsTable).padBottom(3);

            addListeners();

            pack();
            centerWindow();

            textureRegion.focusField();
        }

        private void addListeners()
        {
            okButton.addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    try
                    {
                        listener.ok(textureRegion.getText());
                        fadeOut();
                    }
                    catch (NumberFormatException ignore)
                    {
                    }
                }
            });

            cancelButton.addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    listener.cancel(backup);
                    close();
                }
            });

            InputListener enterListener = new InputListener()
            {
                @Override
                public boolean keyDown(InputEvent event, int keycode)
                {
                    if (keycode == Input.Keys.ENTER && !okButton.isDisabled())
                    {
                        try
                        {
                            listener.ok(textureRegion.getText());
                            fadeOut();
                        }
                        catch (NumberFormatException ignore)
                        {
                        }
                    }
                    return super.keyDown(event, keycode);
                }

                @Override
                public boolean keyUp(InputEvent event, int keycode)
                {
                    listener.changed(textureRegion.getText());
                    return super.keyUp(event, keycode);
                }
            };
            textureRegion.addListener(enterListener);

            textureList.addListener(new TextureList.SelectionChangedListener()
            {
                @Override
                public void selectionChanged(TextureAtlas.AtlasRegion newSelection, int newIndex)
                {
                    listener.changed(newSelection.name);
                    textureRegion.setText(newSelection.name);
                }
            });
        }


        private static String get(EditTextureRegionDialog.Text text)
        {
            return VisUI.getDialogUtilsBundle().get(text.getName());
        }

        private enum Text implements BundleText
        {
            CANCEL
                    {
                        public String getName()
                        {
                            return "cancel";
                        }
                    },
            OK
                    {
                        public String getName()
                        {
                            return "ok";
                        }
                    };

            @Override
            public String get()
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public String format()
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public String format(Object... arguments)
            {
                throw new UnsupportedOperationException();
            }
        }

        public static void showDialog(Stage stage, String title, Listener listener, Assets.Holder<TextureRegion> holder)
        {
            EditTextureRegionDialog dialog = new EditTextureRegionDialog(title, listener, holder);
            stage.addActor(dialog.fadeIn());
        }

        public interface Listener
        {
            void ok(String textureRegion);
            void changed(String textureRegion);
            void cancel(String originalRegion);
        }
    }
}
