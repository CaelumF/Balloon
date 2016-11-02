package com.gmail.caelum119.utils.event;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Caelum on 6/25/14.
 */
public class EventHandler{
  //There is a separate list for each event for quicker access.
  private static HashMap<Class<? extends Event>, ArrayList<Listener>> listeners = new HashMap<Class<? extends Event>, ArrayList<Listener>>();


  public static void addEventListener(Object listener, Object invoker){
    Class<?> klass = listener.getClass();

    while(klass != Object.class){
      List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));

      for(final Method method : allMethods){
        EventSettings listenerAnnotation = method.getAnnotation(EventSettings.class);


        //if listener Annotation isn't null, it must be a instanceof EventSettings.class(AKA annotation)
        if(listenerAnnotation != null){
          Class<? extends Event> listeningFor = (Class<? extends Event>) method.getGenericParameterTypes()[0];
          if(listeningFor != null && Event.class.isAssignableFrom(listeningFor)){
            ArrayList<Listener> listenerList = listeners.get(listeningFor);
            Listener listener1 = new Listener(method, invoker);

            listener1.limit = listenerAnnotation.limit();
            listener1.ignoreCancelled = listenerAnnotation.ignoreCancelled();

            if(listenerList == null){
              listenerList = new ArrayList<Listener>();
              listeners.put(listeningFor, listenerList);
            }

            listenerList.add(listener1);
          }
        }
      }
      klass = klass.getSuperclass();
    }
  }
  //TODO: Make a separate method for reflecting methods with a given annotation.

  public static void throwEvent(Event event){

    ArrayList<Listener> listenerList = listeners.get(event.getClass());
    if(listenerList != null)
      for(Listener listener : listenerList){
        if(listener.limit != 0 && (! listener.ignoreCancelled || ! event.isCancelled())){
          try{
            listener.method.invoke(listener.invoker, event);
          }catch(IllegalAccessException e){
            e.printStackTrace();
          }catch(InvocationTargetException e){
            e.printStackTrace();
          }
          listener.limit--;
        }

      }
  }

  private static class Listener{
    int limit = - 1;
    Method method;
    Object invoker;
    boolean ignoreCancelled;

    private Listener(Method method, Object invoker){
      this.method = method;
      this.invoker = invoker;
    }
  }
}
