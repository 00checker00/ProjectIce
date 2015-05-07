package de.project.ice.screens;

public abstract class BaseScreenAdapter implements BaseScreen {

    /**
     * {@inheritDoc}
     *
     * @return The display priority of the screen, base implementation returns 0
     */
    @Override
    public int getPriority () {
        return 0;
    }

    @Override
    public void show () {
    }

    @Override
    public void update (float delta) {
    }

    @Override
    public void render () {
    }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void pause () {
    }

    @Override
    public void resume () {
    }

    @Override
    public void hide () {
    }

    @Override
    public void dispose () {
    }
}
