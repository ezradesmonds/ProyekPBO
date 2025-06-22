package com.javakaian.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.game.resources.MusicHandler;
import com.javakaian.game.resources.MyAtlas;
import com.javakaian.game.ui.buttons.ButtonFactory;
import com.javakaian.game.ui.buttons.OButton;
import com.javakaian.game.ui.buttons.OButtonListener;
import com.javakaian.game.ui.components.SimpleLayout;
import com.javakaian.game.util.GameConstants;
import com.javakaian.game.util.GameUtils;

import java.util.ArrayList;
import java.util.List;

public class MapSelectState extends State {

    private final String stateName = "SELECT MAP";

    private OButton btnGreenTheme;
    private OButton btnBrownTheme;
    private OButton btnBack;
    private final List<OButton> buttons;
    private final SimpleLayout layout;

    public MapSelectState(StateController stateController) {
        super(stateController);
        glyphLayout.setText(bitmapFont, stateName);

        buttons = new ArrayList<>();
        layout = new SimpleLayout(0, 0, GameConstants.VIRTUAL_WIDTH, GameConstants.GRID_HEIGHT * 4, 50, 80);
        initButtons();
        setListeners();

        // Atur posisi layout agar di tengah
        layout.setPosition(
                (GameConstants.VIRTUAL_WIDTH - layout.getSize().x) / 2,
                GameConstants.VIRTUAL_HEIGHT / 2 - layout.getSize().y / 2
        );
    }

    private void initButtons() {
        // Buat tombol dengan lebar yang lebih besar untuk teks
        final ButtonFactory bf = new ButtonFactory(GameConstants.GRID_WIDTH * 4f, GameConstants.GRID_HEIGHT * 1.5f);
        btnGreenTheme = bf.createOButton("Green Field", MyAtlas.GENERIC_BUTTON, true);
        btnBrownTheme = bf.createOButton("Brown Desert", MyAtlas.GENERIC_BUTTON, true);
        btnBack = bf.createOButton("Back", MyAtlas.GENERIC_BUTTON, true);

        buttons.add(btnGreenTheme);
        buttons.add(btnBrownTheme);
        buttons.add(btnBack);

        layout.addComponents(buttons);
        layout.pack();
    }

    private void setListeners() {
        OButtonListener playListener = (event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                getStateController().setState(StateEnum.PlayState);
                MusicHandler.playBackgroundMusic();
                MusicHandler.stopMenuMusic();
            }
        };

        btnGreenTheme.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                MyAtlas.loadMapTheme(MyAtlas.Theme.GREEN);
                playListener.touchEvent(event, x, y);
            }
        });

        btnBrownTheme.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                MyAtlas.loadMapTheme(MyAtlas.Theme.BROWN);
                playListener.touchEvent(event, x, y);
            }
        });

        btnBack.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                // Kembali ke menu utama
                getStateController().setState(StateEnum.MenuState);
            }
        });
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        super.render(sb, sr);
        Gdx.gl.glClearColor(GameConstants.RED, GameConstants.GREEN, GameConstants.BLUE, GameConstants.ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        GameUtils.renderCenter(stateName, sb, bitmapFont);
        layout.render(sb);
        sb.end();
    }

    @Override
    public void update(float deltaTime) { }

    @Override
    public void updateInputs(float x, float y) { }

    @Override
    public void touchDown(float x, float y, int pointer, int button) {
        buttons.stream().filter(b -> b.contains(x, y)).findFirst().ifPresent(b -> b.touchDown(x, y));
    }

    @Override
    public void touchUp(float x, float y, int pointer, int button) {
        buttons.forEach(b -> b.setPressed(false));
        buttons.stream().filter(b -> b.contains(x, y)).findFirst().ifPresent(b -> b.touchRelease(x, y));
    }

    @Override
    public void scrolled(int amount) { }
}