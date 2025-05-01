package com.javarush.games.ticktacktoe;

import com.javarush.engine.cell.*;

public class TicTacToeGame extends Game {
    private int[][] model = new int[3][3];
    private int currentPlayer;
    private boolean isGameStopped;

    public void initialize(){
        setScreenSize(3,3);
        startGame();
        updateView();
    }
    public void startGame(){
        currentPlayer = 1;
        isGameStopped=false;
        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                model[x][y]=0;
            }
        }
    }

    public void updateView(){
        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                updateCellView(x,y,model[x][y]);
            }
        }
    }
    public void updateCellView(int x, int y, int value){
        if(value ==2){
            setCellValueEx(x, y,Color.WHITE,"O",Color.BLUE);
        } else if (value ==1){
            setCellValueEx(x, y,Color.WHITE,"X",Color.RED);
        } else
            setCellValueEx(x, y,Color.WHITE,"",Color.WHITE);
    }

    public void setSignAndCheck(int x, int y){
        model[x][y]=currentPlayer;
        updateView();

        if(checkWin(x,y,currentPlayer)){
            isGameStopped=true;
            if(currentPlayer==1)
                showMessageDialog(Color.NONE, "You Win!", Color.GREEN, 75);
            if(currentPlayer==2)
                showMessageDialog(Color.NONE, "Game Over", Color.RED, 75);
            return;
        }

        if(!hasEmptyCell()){
            isGameStopped = true;
            showMessageDialog(Color.NONE, " Draw!",  Color.BLUE, 75);
            return;
        }
    }

    public void computerTurn(){
        //ход в центр
        if(model[1][1]==0){
            setSignAndCheck(1,1);
            return;
        }

        //Пробуем выиграт одним ходом
        for(int x=0;x<3;x++)
            for(int y=0;y<3;y++)
                if(checkFutureWin(x,y,1)){
                    setSignAndCheck(x,y);
                    return;
                }
        //Мешаем противнику выиграть
        for(int x=0;x<3;x++)
            for(int y=0;y<3;y++)
                if(checkFutureWin(x,y,2)){
                    setSignAndCheck(x,y);
                    return;
                }

        //ход в любую клетку
        for(int x=0;x<3;x++)
            for(int y=0;y<3;y++)
                if(model[x][y]==0){
                    setSignAndCheck(x,y);
                    return;
                }

    }

    public boolean hasEmptyCell(){
        for(int x=0;x<3;x++)
            for(int y=0;y<3;y++)
                if(model[x][y]==0)
                    return true;
        return false;
    }

    // @Override
    public void onMouseLeftClick(int x,int y){
        if(isGameStopped)
            return;

        if( model[x][y]!=0)
            return;

        setSignAndCheck(x, y);
        currentPlayer = 3 - currentPlayer;

        computerTurn();
        currentPlayer = 3 - currentPlayer;
    }
    public boolean checkWin(int x, int y, int n){ //Проверка на победу в игре
        if(model[x][0]==n && model[x][1]==n && model[x][2]==n){
            return true;
        }
        if(model[0][y]==n && model[1][y]==n && model[2][y]==n){
            return true;
        }
        if(model[0][0] == n && model[1][1] == n && model[2][2]==n)
            return true;
        if(model[2][0] == n && model[1][1] == n && model[0][2]==n)
            return true;
        return false;
    }

    public boolean checkFutureWin(int x, int y, int n){
        if(model [x][y] != 0)
            return false;

        model[x][y]=n;
        boolean isWin = checkWin(x,y,n);
        model[x][y]=0;
        return isWin;

    }

    public void onKeyPress(Key key){
        if(isGameStopped && key == Key.SPACE){
            startGame();
            updateView();
        }

        if(key == Key.ESCAPE){
            startGame();
            updateView();
        }
    }

    //((model[x][0]==n && model[2][y]==n) && (model[x][2]==n && model[0][y]==n)) &&  && ((model[x][2]==n && model[0][y]==n) && (model[x][0]==n && model[2][y]==n))

}