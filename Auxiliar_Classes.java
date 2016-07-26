import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.imageio.*;

class Basic_Variables extends Munhoz_Engine {
   Image bullet_r,bullet_l,backgroung,rifle_r,assault_rifle_r,revolver_r;
   Image rifle_l,assault_rifle_l,revolver_l,rocket_louncher_r,rocket_louncher_l;
   Image explosion_img;
   int obs_counter, bl_counter, pl_counter, drop_counter, explosion_counter;
   Obstacle[] obstacles = new Obstacle[20];
   Player[] player = new Player[4];
   Bullet[] bullets = new Bullet[10];
   Weapon_Drop[] drops = new Weapon_Drop[10];
   Explosion[] explosion = new Explosion[4];
   Basic_Variables(){
      bl_counter=5;
      pl_counter=2;
      obs_counter=17;
      drop_counter=9;
      explosion_counter=4;
      for(int i=0;i<pl_counter;i++){
         player[i]=new Player();
      }

      for(int i=0;i<bl_counter;i++){
         bullets[i]=new Bullet();
      }

      for(int i=0;i<explosion_counter;i++){
         explosion[i]=new Explosion();
      }
   }
}

class Chat /*extends JFrame*/ {
   //JTextField new_message;
   ArrayList<String> messages = new ArrayList<String>();
   ArrayList<Integer> k_count = new ArrayList<Integer>();
   String next_message = new String();

   /*Chat(){

   }*/

   void refresh(){
      int aux = this.messages.size()-20;
      if(aux>0){
         for(int i=0;i<aux;i++){
            this.messages.remove(0);
         }
      }
   }
}

class Weapon extends Munhoz_Engine{
   Image right, left;
   int x,y,damage,cooldown,height,width,speed;
   boolean automatic, exists=true;
}

class Rifle extends Weapon{
   Rifle(Image i_right, Image i_left){
      damage=4;
      cooldown=80;
      x=10;
      y=25;
      speed=44;
      automatic=false;
      right = i_right;
      height=right.getHeight(this);
      width=right.getWidth(this);
      left = i_left;
   }

   Rifle(Basic_Variables var){
      damage=4;
      cooldown=80;
      x=10;
      y=25;
      speed=44;
      automatic=false;
      right = var.rifle_r;
      height=right.getHeight(this);
      width=right.getWidth(this);
      left = var.rifle_l;
   }
   Rifle(){
      damage=4;
      cooldown=80;
      x=10;
      y=25;
      speed=44;
      automatic=false;
      width=29;
   }
}

class Assault_Rifle extends Weapon{
   Assault_Rifle(Image i_right, Image i_left){
      x=20;
      y=25;
      damage=1;
      cooldown=15;
      speed=40;
      automatic=true;
      right = i_right;
      height=right.getHeight(this);
      width=right.getWidth(this);
      left = i_left;
   }
   Assault_Rifle(){
      x=20;
      y=25;
      damage=1;
      cooldown=15;
      speed=40;
      automatic=true;
      width=29;
   }
}

class None extends Weapon{
   None(){
      exists = false;
   }
}

class Revolver extends Weapon{
   Revolver(Image i_right, Image i_left){
      x=25;
      y=25;
      damage=3;
      cooldown=30;
      speed=40;
      automatic=false;
      right = i_right;
      height=right.getHeight(this);
      width=right.getWidth(this);
      left = i_left;
   }
   Revolver(){
      x=25;
      y=25;
      damage=3;
      cooldown=30;
      speed=40;
      automatic=false;
      width=29;
   }
}

class Rocket_Louncher extends Weapon{
   Rocket_Louncher(Image i_right, Image i_left){
      x=0;
      y=5;
      damage=40;
      cooldown=100;
      speed=20;
      automatic=false;
      right = i_right;
      height=right.getHeight(this);
      width=right.getWidth(this);
      left = i_left;
   }
   Rocket_Louncher(){
      x=0;
      y=5;
      damage=40;
      cooldown=100;
      speed=20;
      automatic=false;
      width=29;
   }
}

class Bullet extends Munhoz_Engine{
   Speed speed = new Speed();
   Integer x,y,damage;
   Player whose= new Player();
   static int bullet_count=0,height,width;
   boolean show=false;
   Image image, r, l;
   void setBullet(Image img, int new_x, int new_y, double new_speed, int new_damage){
      image = img;
      height=image.getHeight(this);
      width=image.getWidth(this);
      x=new_x;
      if(new_speed<0)
         x-=width;
      y=new_y+height/2;
      speed.right=new_speed;
      damage = new_damage;
   }
   void setBullet(int new_x, int new_y, double new_speed, int new_damage){
      x=new_x;
      if(new_speed<0)
         x-=width;
      y=new_y+height/2;
      speed.right=new_speed;
      damage = new_damage;
   }
   Bullet(){
      x=0;
      y=0;
      show=false;
      speed.right=0.0;
   }
}

class Speed {
   Double down, right, ac;
   void recalculate(){
      down+=ac;
   }
}

class Player {
   static Image right, left;
   static Image[] hp_bar = new Image[4];
   boolean to_right, can_jump, can_shot;
   Integer x, y;
   int hp, height, width, shot_timer, score=0;
   Speed speed = new Speed();
   Weapon weapon;
}

class Obstacle extends Munhoz_Engine{
   Image image;
   int x, y, width, height;
   Obstacle(int new_x, int new_y, String img_file){
      x=new_x;
      y=new_y;
      try {
        image = ImageIO.read(new File(img_file));
        height=image.getHeight(this);
        width=image.getWidth(this);
      } catch (IOException e) {
        System.out.println("A imagem nao pode ser carregada.");
      }
   }
   Obstacle(int new_x, int new_y, int new_width, int new_height){
      x=new_x;
      y=new_y;
      height=new_height;
      width=new_width;
   }
}

class Weapon_Drop{
   Weapon weapon;
   int x, y, width, height, timer;
   Weapon_Drop (int new_x, int new_y, String str, Image i_right, Image i_left){
      switch(str){
         case "rifle":
         System.out.println("rifle");
         weapon = new Rifle(i_right, i_left);
         break;

         case "assault_rifle":
         System.out.println("assault_rifle");
         weapon = new Assault_Rifle(i_right, i_left);
         break;

         case "rocket_louncher":
         System.out.println("rocket_louncher");
         weapon = new Rocket_Louncher(i_right, i_left);
         break;

         case "revolver":
         System.out.println("revolver");
         weapon = new Revolver(i_right, i_left);
      }
      x = new_x;
      y = new_y;
      width = weapon.width;
      height = 30;
   }

   Weapon_Drop (int new_x, int new_y, String str, Basic_Variables var){
      switch(str){
         case "rifle":
         weapon = new Rifle(var);
         break;

         case "assault_rifle":
         weapon = new Assault_Rifle(var.assault_rifle_r, var.assault_rifle_l);
         break;

         case "rocket_louncher":
         weapon = new Rocket_Louncher(var.rocket_louncher_r, var.rocket_louncher_l);
         break;

         case "revolver":
         weapon = new Revolver(var.revolver_r, var.revolver_l);
      }
      x = new_x;
      y = new_y;
      width = weapon.width;
      height = 30;
   }

   Weapon_Drop (int new_x, int new_y, String str){
      switch(str){
         case "rifle":
         weapon = new Rifle();
         break;

         case "assault_rifle":
         weapon = new Assault_Rifle();
         break;

         case "rocket_louncher":
         weapon = new Rocket_Louncher();
         break;

         case "revolver":
         weapon = new Revolver();
      }
      x = new_x;
      y = new_y;
      width = weapon.width;
      height = 30;
   }
}

class Explosion extends Munhoz_Engine{
   Image image;
   static int counter;
   int timer, x, y, width, height;
   boolean show;
   Explosion(){

   }
   Explosion(int new_x, int new_y, Image img){
      x=new_x;
      y=new_y;
      timer=6;
      show=true;
      image = img;
      height=image.getHeight(this);
      width=image.getWidth(this);
   }
   Explosion(int new_x, int new_y){
      x=new_x;
      y=new_y;
      timer=6;
      show=true;
   }
}
