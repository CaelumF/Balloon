//import com.gmail.caelum119.utils.event.EventHandler;
//import com.gmail.caelum119.utils.event.events.control.MouseMoveEvent;
//
////package com.gmail.caelum119.utils;
////
////
////import java.awt.Robot;
////import java.awt.event.*;
////import java.util.ArrayList;
////import java.util.HashMap;
//////Todo: Clean up this crap
//////Legend has it there are thousands of people lost in here still looking for the way out.
////public class Control implements KeyListener, MouseListener, MouseMotionListener
////{
////    private static ArrayList<Listener> listeners;
////    ;
////    private static ArrayList<aimEvent> aimListeners;
////    private static ArrayList<clickEvent> clickListeners;
////    private static HashMap down = new HashMap();
////    private static boolean leftClicking, rightClicking, freeMouse, scrollClicking;
////    private static double aimX, aimY, sensitve = 10;
////    private Robot robert;
////
////    public static Control thisOne = new Control(Window.width, Window.height);
////
////
////    public Control(int windowWidth, int windowHeight)
////    {
////        if(thisOne != null)
////            return;
////
////        listeners = new ArrayList<Listener>();
////        aimListeners = new ArrayList<aimEvent>();
////        clickListeners = new ArrayList<clickEvent>();
////
////        try
////        {
////            robert = new Robot();
////        }
////        catch(AWTException e)
////        {
////            e.printStackTrace();
////        }
////
////        thisOne = this;
////
////    }
////
////    //Add listener
////    public static void addListener(Key key, keyPressEvent listener)
////    {
////        listeners.add(new Listener(key, listener));
////    }
////
////    public static void addAimListener(aimEvent listener)
////    {
////        aimListeners.add(listener);
////    }
////
////    public static void addClickListener(clickEvent listener)
////    {
////        clickListeners.add(listener);
////    }
////
////    public static boolean isDown(int keyCode)
////    {
////        if(down.containsKey(keyCode))
////        {
////            if(down.get(keyCode).equals(true))
////                return true;
////        }
////
////        return false;
////
////    }
////
////    public static boolean isDown(Key keyCode)
////    {
////        if(down.containsKey(keyCode.getKeyCode()))
////        {
////            if(down.get(keyCode.getKeyCode()).equals(true))
////                return true;
////        }
////
////
////        return false;
////
////    }
////
////    public static boolean isClicking(int mouse)
////    {
////        if(mouse == 1)
////            return leftClicking;
////        if(mouse == 2)
////            return rightClicking;
////        if(mouse == 3)
////            return scrollClicking;
////
////        return false;
////    }
////
////    public static double getAimX()
////    {
////        return aimX;
////    }
////
////    public static double getAimY()
////    {
////        return aimY;
////    }
////
////    @Override
////    public void keyPressed(KeyEvent e)
////    {
////        int key = e.getKeyCode();
////
////        down.put(key, true);
////        for(Listener listener : listeners)
////        {
////            if(listener.getKey() == key)
////            {
////                listener.sendPress(key);
////            }
////        }
////
////    }
////
////    public void aimUp(int amount)
////    {
////        for(aimEvent ae : aimListeners)
////        {
////            ae.aimedY(amount);
////        }
////
////    }
////
////    @Override
////    public void keyReleased(KeyEvent e)
////    {
////
////        down.put(e.getKeyCode(), false);
////        //*Sets Q to free mouse
////        //        if(e.getKeyCode()==81)
////        //        {
////        //            freeMouse=freeMouse==false;
////        //
////        //        }
////    }
////
////    @Override
////    public void keyTyped(KeyEvent e)
////    {
////        // nothing here
////    }
////
////    @Override public void mouseClicked(MouseEvent e)
////    {
////
////
////    }
////
////    @Override public void mousePressed(MouseEvent e)
////    {
////        for(clickEvent cl : clickListeners)
////        {
////            cl.clicked(e);
////        }
////
////        int btn = e.getButton();
////
////        if(btn == 1)
////            leftClicking = true;
////        if(btn == 2)
////            scrollClicking = true;
////        if(btn == 3)
////            rightClicking = true;
////
////    }
////
////    @Override public void mouseReleased(MouseEvent e)
////    {
////        for(clickEvent cl : clickListeners)
////        {
////            cl.released(e);
////        }
////
////        int btn = e.getButton();
////
////        if(btn == 1)
////            leftClicking = false;
////        if(btn == 2)
////            scrollClicking = false;
////        if(btn == 3)
////            rightClicking = false;
////    }
////
////    @Override public void mouseEntered(MouseEvent e)
////    {
////        Debug.info("lllllll");
////    }
////
////    @Override public void mouseExited(MouseEvent e)
////    {
////
////    }
////
////    @Override public void mouseDragged(MouseEvent e)
////    {
////
////        if(! freeMouse)
////        {
////
////        }
////
////    }
////
////
////    double normalizeAngle(double angle)
////    {
////        double newAngle = angle;
////        while(newAngle <= 0) newAngle += 360;
////        while(newAngle > 360) newAngle -= 360;
////        return newAngle;
////    }
////
////    //UN
////
//class Control{
//    @Override public void mouseMoved(MouseEvent e){
//
//        if(! freeMouse){
//            aimX += (((e.getX() - (Window.width / 2)) * 0.1));
//            aimY += (((e.getY() - (Window.height / 2)) * 0.1));
//
//
//            //            aimX = normalizeAngle(aimX);
//            aimY = normalizeAngle(aimY);
//            //            if(aimX>360)
//            //                aimX = -360;
//            //            if(aimX > 180)
//            //                aimX = -180;
//
//            if(aimY > 89 && aimY < 120)
//                aimY = 89;
//
//            if(aimY < 281 && aimY > 120)
//                aimY = 281;
//
//            robert.mouseMove(Window.x + (Window.width / 2), (Window.height / 2) + Window.y);
//        }
//        EventHandler.throwEvent(new MouseMoveEvent(e, aimX, aimY));
//    }
//}
////
////    public static enum Key
////    {
////        W(87), A(65), S(83), D(68), E(69), Q(81), UP(38), DOWN(40), LEFT(37), RIGHT(39), ESC(27), R(82), CTRL(17), F(70), G(71), J(74), T(84), Y(89), U(85), SHIFT(16);
////
////        private int keyCode;
////
////
////        Key(int keyCode)
////        {
////            this.keyCode = keyCode;
////
////        }
////
////        public int getKeyCode()
////        {
////            return this.keyCode;
////        }
////    }
////
////    //Interfaces
////    public interface keyPressEvent
////    {
////        public void sendKeyPressEvent(int e);
////    }
////
////    public interface clickEvent
////    {
////        public void clicked(MouseEvent e);
////
////        public void released(MouseEvent e);
////    }
////
////    public interface aimEvent
////    {
////        public void aimedY(int amount);
////    }
////
////    public static class Listener
////    {
////        private int key;
////
////        private keyPressEvent listener;
////
////        public Listener(int key, keyPressEvent listenerq)
////        {
////            this.key = key;
////            this.listener = listener;
////        }
////
////        public Listener(Key key, keyPressEvent listener)
////        {
////            this.key = key.getKeyCode();
////            this.listener = listener;
////        }
////
////
////        public int getKey()
////        {
////            return key;
////        }
////
////        public void sendPress(int e)
////        {
////            listener.sendKeyPressEvent(e);
////        }
////
////    }
////
////    public Robot getRobert()
////    {
////        return robert;
////    }
////
////    public static Control getThisOne()
////    {
////        return thisOne;
////    }
////
////
////}
