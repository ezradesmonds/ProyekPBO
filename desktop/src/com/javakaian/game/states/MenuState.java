package com.javakaian.game.states;

import static com.javakaian.game.util.GameConstants.ALPHA;
import static com.javakaian.game.util.GameConstants.BLUE;
import static com.javakaian.game.util.GameConstants.GREEN;
import static com.javakaian.game.util.GameConstants.RED;

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
import static com.javakaian.game.resources.MyAtlas.backgroundSprite;
import java.util.ArrayList;
import java.util.List;

public class MenuState extends State {

    private final String stateName = "Tank Defense";

    private OButton btnPlay;
    private OButton btnOptions;
//    private OButton btnCredits;
    private final List<OButton> buttons;
    private final SimpleLayout layout;

    public MenuState(StateController stateController) {
        super(stateController);
        glyphLayout.setText(bitmapFont, stateName);

        final float buttonWidth = GameConstants.GRID_WIDTH * 1.5f;
        final float spacing = 110f; // Jarak antar tombol
        final int buttonCount = 2;

        // Lebar total = (2 * lebar tombol) + (1 * jarak)
        final float totalWidth = (buttonCount * buttonWidth) + spacing;
        // Posisi X = (Lebar Layar - Lebar Total) / 2
        final float startX = (GameConstants.VIRTUAL_WIDTH - totalWidth) / 2;

        layout = new SimpleLayout(
                startX, // Posisi X baru yang sudah dihitung
                GameConstants.GRID_HEIGHT * 4, // Posisi Y tetap di tengah
                totalWidth, // Lebar layout baru
                GameConstants.GRID_HEIGHT * 3,
                (int)spacing,
                50
        );

        buttons = new ArrayList<>();
        initButtons();
        setListeners();
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        super.render(sb, sr);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        backgroundSprite.setSize(GameConstants.VIRTUAL_WIDTH, GameConstants.VIRTUAL_HEIGHT);
        backgroundSprite.setPosition(0, 0);
        backgroundSprite.draw(sb);
        GameUtils.renderCenter(stateName, sb, bitmapFont);
        layout.render(sb);
        sb.end();
    }

    @Override
    public void update(float deltaTime) {
    }

    private void initButtons() {
        final ButtonFactory bf = new ButtonFactory(GameConstants.GRID_WIDTH * 1.5f,
                GameConstants.GRID_HEIGHT * 1.5f);
        btnPlay = bf.createOButton(MyAtlas.MENU_PLAY);
        btnOptions = bf.createOButton("OPTIONS", MyAtlas.GENERIC_BUTTON, true);
//        btnCredits = bf.createOButton("CREDITS", MyAtlas.GENERIC_BUTTON, true);

        buttons.add(btnPlay);
        buttons.add(btnOptions);
//        buttons.add(btnCredits);

        layout.addComponents(buttons);
        layout.pack();
    }

    private void setListeners() {
        btnPlay.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                getStateController().setState(StateEnum.MapSelectState);
//                getStateController().setState(StateEnum.PlayState);
//                MusicHandler.playBackgroundMusic();
//                MusicHandler.stopMenuMusic();
            }
        });
        btnOptions.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                getStateController().setState(StateEnum.OptionState);
            }
        });

//        btnCredits.setButtonListener((event, x, y) -> {
//            if (event == OButtonListener.TouchEvent.RELEASE) {
//                getStateController().setState(StateEnum.CreditsState);
//            }
//        });

    }

    @Override
    public void updateInputs(float x, float y) {
    }

    @Override
    public void touchDown(float x, float y, int pointer, int button) {
        buttons.stream()
                .filter(b -> b.contains(x, y))
                .findFirst()
                .ifPresent(b -> b.touchDown(x, y));
    }

    @Override
    public void touchUp(float x, float y, int pointer, int button) {
        buttons.forEach(b -> b.setPressed(false));
        buttons.stream()
                .filter(b -> b.contains(x, y))
                .findFirst()
                .ifPresent(b -> b.touchRelease(x, y));
    }

    @Override
    public void scrolled(int amount) {
    }

}
