package de.project.ice.editor.editors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.i18n.BundleText;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.*;
import de.project.ice.editor.TextureList;
import de.project.ice.utils.Assets;
import de.project.ice.utils.DialogListener;


public class AnimationHolderEditor extends HolderEditor<Animation>
{
    @Override
    protected void onEdit()
    {
        EditAnimationDialog.showDialog(getStage(), "Edit animation", new DialogListener<AnimationDialogResult>()
        {
            @Override
            public void onResult(AnimationDialogResult result)
            {
                Assets.Holder<Animation> newHolder = Assets.createAnimation(
                        result.name,
                        result.frameTime,
                        result.playMode);
                setHolderData(newHolder.data);
                setHolderName(newHolder.name);
            }

            @Override
            public void onChange(AnimationDialogResult result)
            {
            }

            @Override
            public void onCancel()
            {
            }
        }, value);
    }

    public static class EditAnimationDialog extends VisDialog
    {
        private DialogListener<AnimationDialogResult> listener;
        private VisTextField textureRegion;
        private TextureList textureList;
        private VisTextField frameTime;
        private VisSelectBox<Animation.PlayMode> playMode;
        private VisTextButton okButton;
        private VisTextButton cancelButton;

        public EditAnimationDialog(String title, DialogListener<AnimationDialogResult> listener, Assets.Holder<Animation> holder)
        {
            super(title);
            this.listener = listener;

            TableUtils.setSpacingDefaults(this);

            Table contentTable = getContentTable();

            textureRegion = new VisTextField(holder.name);
            contentTable.add(new VisLabel("Texture Region: "));
            contentTable.add(textureRegion).expand().fill().row();

            textureList = new TextureList();
            textureList.setWidth(Float.MAX_VALUE);

            VisScrollPane scrollPane = new VisScrollPane(textureList);

            contentTable.add(scrollPane).maxHeight(200).colspan(2).expandX().fill().row();

            frameTime = new VisValidatableTextField(new Validators.FloatValidator());
            if (holder.data != null)
            {
                frameTime.setText(String.valueOf(holder.data.getFrameDuration()));
            }
            contentTable.add(new VisLabel("Frame Time: "));
            contentTable.add(frameTime).expand().fill().row();

            playMode = new VisSelectBox<Animation.PlayMode>();
            playMode.setItems(Animation.PlayMode.values());
            if (holder.data != null)
            {
                playMode.setSelected(holder.data.getPlayMode());
            }
            contentTable.add(new VisLabel("Mode: "));
            contentTable.add(playMode).expand().fill();

            add(contentTable).padTop(3).spaceBottom(4);
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
                listener.onResult(new AnimationDialogResult(
                        textureRegion.getText(),
                        Float.parseFloat(frameTime.getText()),
                        playMode.getSelected()));
            }
            else
            {
                listener.onCancel();
            }
        }

        private void addListeners()
        {
            textureList.addListener(new TextureList.SelectionChangedListener()
            {
                @Override
                public void selectionChanged(TextureAtlas.AtlasRegion newSelection, int newIndex)
                {
                    textureRegion.setText(newSelection.name);
                }
            });
        }

        private static String get(EditAnimationDialog.Text text)
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

        public static void showDialog(Stage stage, String title, DialogListener<AnimationDialogResult> listener, Assets.Holder<Animation> holder)
        {
            EditAnimationDialog dialog = new EditAnimationDialog(title, listener, holder);
            dialog.show(stage);
        }
    }

    private static class AnimationDialogResult
    {
        public final String name;
        public final float frameTime;
        public final Animation.PlayMode playMode;

        public AnimationDialogResult(String name, float frameTime, Animation.PlayMode playMode)
        {
            this.name = name;
            this.frameTime = frameTime;
            this.playMode = playMode;
        }
    }
}
