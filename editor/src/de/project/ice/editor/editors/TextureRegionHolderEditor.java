package de.project.ice.editor.editors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.i18n.BundleText;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextField;
import de.project.ice.editor.TextureList;
import de.project.ice.utils.Assets;
import de.project.ice.utils.DialogListener;


public class TextureRegionHolderEditor extends HolderEditor<TextureRegion>
{
    @Override
    protected void onEdit()
    {
        final String currentTexture = value.name;
        EditTextureRegionDialog.showDialog(getStage(), "Select texture", new DialogListener<String>()
        {
            @Override
            public void onResult(String textureRegion)
            {
                Assets.Holder<TextureRegion> newHolder = Assets.findRegion(textureRegion);
                setHolderName(newHolder.name);
                setHolderData(newHolder.data);
            }

            @Override
            public void onChange(String textureRegion)
            {
                Assets.Holder<TextureRegion> newHolder = Assets.findRegion(textureRegion);
                setHolderName(newHolder.name);
                setHolderData(newHolder.data);
            }

            @Override
            public void onCancel()
            {
                Assets.Holder<TextureRegion> newHolder = Assets.findRegion(currentTexture);
                setHolderName(newHolder.name);
                setHolderData(newHolder.data);
            }
        }, value.name);
    }

    private static class EditTextureRegionDialog extends VisDialog
    {
        private DialogListener<String> listener;
        private VisTextField textureRegion;
        private final TextureList textureList;

        public EditTextureRegionDialog(String title, DialogListener<String> listener, String currentTexture)
        {
            super(title);
            this.listener = listener;

            TableUtils.setSpacingDefaults(this);

            addCloseButton();

            Table contentTable = getContentTable();

            textureRegion = new VisTextField(currentTexture);
            contentTable.add(new VisLabel("Texture Region: "));
            contentTable.add(textureRegion).expandX().fill().row();

            textureList = new TextureList();
            textureList.setWidth(Float.MAX_VALUE);

            VisScrollPane scrollPane = new VisScrollPane(textureList);

            contentTable.add(scrollPane).maxHeight(200).colspan(2).expandX().fill().row();

            row();

            addListeners();

            textureRegion.focusField();

            button(get(Text.CANCEL), "cancel");
            key(Input.Keys.ESCAPE, "cancel");
            button(get(Text.OK), "ok");
            key(Input.Keys.ENTER, "ok");
        }

        @Override
        public VisDialog show(Stage stage, Action action)
        {
            VisDialog result = super.show(stage, action);
            stage.setScrollFocus(textureList);
            return result;
        }

        @Override
        protected void result(Object object)
        {
            if ("ok".equals(object))
            {
                listener.onResult(textureRegion.getText());
            }
            else
            {
                listener.onCancel();
            }
        }

        private void addListeners()
        {
            textureRegion.addListener(new InputListener()
            {
                @Override
                public boolean keyUp(InputEvent event, int keycode)
                {
                    listener.onChange(textureRegion.getText());
                    return super.keyUp(event, keycode);
                }
            });

            textureList.addListener(new TextureList.SelectionChangedListener()
            {
                @Override
                public void selectionChanged(TextureAtlas.AtlasRegion newSelection, int newIndex)
                {
                    listener.onChange(newSelection.name);
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

        public static void showDialog(Stage stage, String title, DialogListener<String> listener, String currentTexture)
        {
            EditTextureRegionDialog dialog = new EditTextureRegionDialog(title, listener, currentTexture);
            dialog.show(stage);
        }
    }
}
