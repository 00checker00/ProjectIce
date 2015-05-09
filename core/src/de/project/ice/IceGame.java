package de.project.ice;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import javafx.scene.AmbientLight;

/**
 * Grafik Branch
 */
public class IceGame extends ApplicationAdapter {
    /*
    SpriteBatch batch;
    Texture img;
    */

    private OrthographicCamera camera;
    private ModelBatch modelBatch;
    private ModelBuilder modelBuilder;
    private Model box;
    private ModelInstance modelInstance;
    private Environment environment;



    @Override
    public void create() {
        /*
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        */
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 20f);
        camera.lookAt(0f, 0f, 0f); // look at Origin
        camera.near = 0.5f;
        camera.far = 20f;

        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();
        box = modelBuilder.createBox(20f, 20f, 10f,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(box, 0, 0, 0); // small instance of the whole box model!

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));

       // box.dispose();
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        modelBatch.begin(camera);
        modelBatch.render(modelInstance);
        modelBatch.end();

        /*
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
        */
    }

}
