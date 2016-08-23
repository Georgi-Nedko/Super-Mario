package com.nikolovg.mariobros.tools;

import com.nikolovg.mariobros.MarioBros;
import com.nikolovg.mariobros.sprites.Mario;
import com.nikolovg.mariobros.states.GameState;

import java.util.Stack;

/**
 * Created by Freeware Sys on 23.8.2016 г..
 */
public class GameStateManager {
    private MarioBros game;
    private Stack<GameState> states;

    public enum State{
        MAIN_MENU,
        PLAY,
    }

    public GameStateManager(final MarioBros game){
        this.game = game;
        this.states = new Stack<GameState>();
        this.setState(State.MAIN_MENU);
    }

    public void update(float dt){
        //states.peek().update(dt);
    }

  //  public void render(){
       // states.peek().render();

    public void dispose(){
        for(GameState gs : states){
            //gs.dispose();
        }
        states.clear();
    }

    public MarioBros getGame(){
        return game;
    }

   // public void resize(int width, int height){
       // states.peek().resize(width, height);
   // }


    public void setState(State state){
       // states.pop().dispose();
        states.push(getState(state));
    }

    private GameState getState(State state){
        return null;
    }
}
