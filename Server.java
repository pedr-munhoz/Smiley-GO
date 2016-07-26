import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.imageio.*;

public class Server extends Munhoz_Engine {
   // Variables declaration
   Basic_Variables var = new Basic_Variables();
   Chat chat = new Chat();
   int aux, right, left, up, fire, right2, left2, up2, fire2;
   Double nhe;
   String clientInput;
   ServerSocket welcomeSocket;
   ServerSocket welcomeSocket2;
   Socket conectionSocket;
   Socket conectionSocket2;
   BufferedReader clientString;
   BufferedReader clientString2;
   DataOutputStream serverToClient;
   DataOutputStream serverToClient2;

   double colisionH(Player p, Double distance){
      Player aux = new Player();
      aux.x=p.x;
      aux.y=p.y;
      aux.width=p.width;
      aux.height=p.height;

      if(distance+aux.x+aux.width>window.width || distance+aux.x<0){
         if(distance>0){
            return window.width-aux.x-aux.width;
         }
         else
            return -aux.x;
      }
      else
         aux.x+=distance.intValue();

      for(int i=0;i<var.obs_counter;i++){
         if(aux.x>var.obstacles[i].x-aux.width){
             if(aux.x<var.obstacles[i].x+var.obstacles[i].width){
               if(aux.y>var.obstacles[i].y-aux.height){
                  if(aux.y<var.obstacles[i].y+var.obstacles[i].height){
                     return 0;
                  }
               }
            }
         }
      }

      return distance;
   }

   void hit_drop(Player p){
      for(int i=0;i<var.drop_counter;i++){
         if(var.drops[i].timer==0){
            if(p.x>(var.drops[i].x-var.drops[i].width/2)-p.width && p.x<(var.drops[i].x-var.drops[i].width/2)+var.drops[i].width/2 && p.y>(var.drops[i].y-var.drops[i].height)-p.height && p.y<(var.drops[i].y-var.drops[i].height)+var.drops[i].height){
               p.weapon = var.drops[i].weapon;
               var.drops[i].timer = 250;
            }
         }
      }
   }

   void set_drops(){
      var.drops[0]=new Weapon_Drop(window.width/2,window.height-50,"rocket_louncher");
      var.drops[1]=new Weapon_Drop(window.width/2,window.height-170,"rifle");
      var.drops[2]=new Weapon_Drop(window.width/2,window.height-410,"assault_rifle");
      var.drops[3]=new Weapon_Drop(50,window.height-170,"revolver");
      var.drops[4]=new Weapon_Drop(window.width-50,window.height-170,"revolver");
      var.drops[5]=new Weapon_Drop(210,window.height-410,"revolver");
      var.drops[6]=new Weapon_Drop(window.width-210,window.height-410,"revolver");
      var.drops[7]=new Weapon_Drop(300,window.height-530,"revolver");
      var.drops[8]=new Weapon_Drop(window.width-300,window.height-530,"revolver");

      for(int i=0;i<var.drop_counter;i++){
         var.drops[i].timer=0;
      }
   }

   void hit(Bullet b){
      for(int i=0;i<var.pl_counter;i++){
         if(b.x>var.player[i].x-b.width){
             if(b.x<var.player[i].x+var.player[i].width){
               if(b.y>var.player[i].y-b.height){
                  if(b.y<var.player[i].y+var.player[i].height){
                     var.player[i].hp-=b.damage;
                     b.show=false;
                     if(var.player[i].hp<1){
                        { // explosion
                           if(b.damage==40){
                              var.explosion[var.explosion[0].counter]=new Explosion(var.player[i].x,var.player[i].y-10);
                              var.explosion[0].counter++;
                              if(var.explosion[0].counter>=4)
                                 var.explosion[0].counter=0;
                           }
                        }
                        spawn(i);
                        b.whose.score++;
                     }
                  }
               }
            }
         }
      }
   }

   double colisionV(Player p, Double distance){
      Player aux = new Player();
      aux.x=p.x;
      aux.y=p.y;
      aux.width=p.width;
      aux.height=p.height;

      if(distance+aux.y+aux.height>window.height || distance+aux.y<0){
         if(distance+aux.y+aux.height>window.height){
            spawn(p);
            if(p.score>0)
               p.score--;
            }
      }
      else
         aux.y+=distance.intValue();

      for(int i=0;i<var.obs_counter;i++){
         if(aux.x>var.obstacles[i].x-aux.width){
             if(aux.x<var.obstacles[i].x+var.obstacles[i].width){
               if(aux.y>var.obstacles[i].y-aux.height){
                  if(aux.y<var.obstacles[i].y+var.obstacles[i].height){
                     if(distance>0){
                        p.can_jump=true;
                     }
                     return 0.0;
                  }
               }
            }
         }
      }
      return distance;
   }

   void gravity(Player p){
      if(p.speed.down==0)
         p.speed.down=0.5;
      Double d = colisionV(p, p.speed.down);
      if(d!=0){
         p.y+=d.intValue();
         p.speed.recalculate();
         p.can_jump=false;
      }
      else{
         p.speed.down=0.0;
      }
   }

   void spawn(int x){
      switch(x){
         case 0:
         var.player[x].x=20;
         var.player[x].to_right=true;
         var.player[x].y=0;
         break;

         case 1:
         var.player[x].x=window.width-20-var.player[1].width;
         var.player[x].to_right=false;
         var.player[x].y=0;
         break;

         case 2:
         var.player[x].x=window.width-20-var.player[1].width;
         var.player[x].to_right=false;
         var.player[x].y=window.height/2;
         break;

         case 3:
         var.player[x].x=20;
         var.player[x].to_right=true;
         var.player[x].y=window.height/2;
      }
      var.player[x].can_shot=true;
      var.player[x].speed.ac=0.5;
      var.player[x].speed.down=0.0;
      var.player[x].shot_timer=0;;
      var.player[x].hp=4;
   }

   void spawn(Player p){
      p.x=window.width/2-p.width/2;
      p.to_right=true;
      p.y=0;
      p.speed.ac=0.5;
      p.speed.down=0.0;
      p.shot_timer=0;
      p.hp=2;
      p.can_shot=true;
   }

   void setObstacles(){
      var.obstacles[0]=new Obstacle((window.width-900)/2,window.height-50,900,14);
      var.obstacles[1]=new Obstacle((window.width-450)/2,window.height-170,450,14);
      var.obstacles[2]=new Obstacle(0,window.height-170,200,14);
      var.obstacles[3]=new Obstacle(window.width-200,window.height-170,200,14);
      var.obstacles[4]=new Obstacle(0,window.height-620,14,450);
      var.obstacles[5]=new Obstacle(window.width-14,window.height-620,14,450);
      var.obstacles[6]=new Obstacle(130,window.height-290,350,14);
      var.obstacles[7]=new Obstacle(window.width-480,window.height-290,350,14);
      var.obstacles[8]=new Obstacle(220,window.height-410,14,120);
      var.obstacles[9]=new Obstacle(window.width-234,window.height-410,14,120);
      var.obstacles[10]=new Obstacle(150,window.height-410,120,14);
      var.obstacles[11]=new Obstacle(window.width-280,window.height-410,120,14);
      var.obstacles[12]=new Obstacle((window.width-200)/2,window.height-410,200,14);
      var.obstacles[13]=new Obstacle(((window.width-200)/2-270)/2+270,window.height-530,14,120);
      var.obstacles[14]=new Obstacle(window.width-((window.width-200)/2-270)/2-270,window.height-530,14,120);
      var.obstacles[15]=new Obstacle(14,window.height-530,450,14);
      var.obstacles[16]=new Obstacle(window.width-450-14,window.height-530,450,14);
   }

   void shot(Player p, Graphics g){
      if(p.to_right){
         var.bullets[var.bullets[0].bullet_count].setBullet(p.x+p.weapon.x+p.weapon.width, p.y+p.weapon.y-5,p.weapon.speed,p.weapon.damage);
      }
      else{
         var.bullets[var.bullets[0].bullet_count].setBullet(p.x-p.weapon.width+p.width-p.weapon.x-var.bullets[0].width, p.y+p.weapon.y-5,-p.weapon.speed,p.weapon.damage);
      }
      var.bullets[var.bullets[0].bullet_count].show=true;
      var.bullets[var.bullets[0].bullet_count].whose=p;
      var.bullets[0].bullet_count++;
      if(var.bullets[0].bullet_count>=var.bl_counter)
         var.bullets[0].bullet_count=0;
   }

   double bullet_movement(Bullet b){
      Bullet aux = new Bullet();
      aux.x=b.x;
      aux.y=b.y;
      aux.width=b.width;
      aux.height=b.height;

      for(int i=0;i<var.obs_counter;i++){
         if(aux.x>var.obstacles[i].x-aux.width){
             if(aux.x<var.obstacles[i].x+var.obstacles[i].width){
               if(aux.y>var.obstacles[i].y-aux.height){
                  if(aux.y<var.obstacles[i].y+var.obstacles[i].height){
                     if(b.damage==40){
                        var.explosion[var.explosion[0].counter]=new Explosion(b.x,b.y);
                        var.explosion[0].counter++;
                        if(var.explosion[0].counter>=4)
                           var.explosion[0].counter=0;
                     }
                     return 0;
                  }
               }
            }
         }
      }

      if(b.speed.right+aux.x+aux.width>window.width || b.speed.right+aux.x<0){
         if(b.speed.right>0){
            return window.width-aux.x-aux.width;
         }
         else{
            return -aux.x;
         }
      }
      else
         aux.x+=b.speed.right.intValue();

      for(int i=0;i<var.obs_counter;i++){
         if(aux.x>var.obstacles[i].x-aux.width && aux.x<var.obstacles[i].x+var.obstacles[i].width && aux.y>var.obstacles[i].y-aux.height && aux.y<var.obstacles[i].y+var.obstacles[i].height){
            if(b.damage==40){
               var.explosion[var.explosion[0].counter]=new Explosion(b.x,b.y);
               var.explosion[0].counter++;
               if(var.explosion[0].counter>=4)
                  var.explosion[0].counter=0;
            }
            return 0;
         }
      }
      return b.speed.right;
   }

   public void init(){
      setSize(1300, 690);
      for(int i=0;i<var.pl_counter;i++){
         try {
            var.player[i].right = ImageIO.read(new File("assets/blank_1_r.gif"));
            var.player[i].height = var.player[i].right.getHeight(this);
            var.player[i].width = var.player[i].right.getWidth(this);
         } catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }
         var.player[i].weapon = new Revolver();
         var.player[i].weapon.width = 29;
      }
      for(int i=0;i<var.bl_counter;i++){
         var.bullets[i]=new Bullet();
      }
      chat.messages.add("inicio");
      for (int i=0; i<var.pl_counter; i++) {
         spawn(i);
      }
      setObstacles();
      { // Chat
         try {
            welcomeSocket = new ServerSocket(9090);
            welcomeSocket2 = new ServerSocket(9000);
         } catch (IOException e) {
            System.out.println("Can't conect");
         }
      }
      var.bullets[0].width=30;
      var.bullets[0].height=8;
      set_drops();
   }

   public void paint(Graphics g) {
      { // Moviment
         Double w;
         { // key listeners
            if(up==1 && var.player[0].can_jump){
               var.player[0].speed.down=-12.0;
               var.player[0].can_jump=false;
            }
            if(right==1){
               var.player[0].to_right=true;
               w = colisionH(var.player[0], 5.0);
               var.player[0].x += w.intValue();
            }
            else if(left==1){
               var.player[0].to_right=false;
               w = colisionH(var.player[0], -5.0);
               var.player[0].x += w.intValue();
            }
            if(fire==1 && var.player[0].shot_timer==0 && var.player[0].can_shot){
               var.player[0].shot_timer=var.player[0].weapon.cooldown;
               var.player[0].can_shot=false;
            }
            if(fire==0 || var.player[0].weapon.automatic){
               var.player[0].can_shot=true;
            }

            if(up2==1 && var.player[1].can_jump){
               var.player[1].speed.down=-12.0;
               var.player[1].can_jump=false;
            }
            if(right2==1){
               var.player[1].to_right=true;
               w = colisionH(var.player[1], 5.0);
               var.player[1].x += w.intValue();
            }
            else if(left2==1){
               var.player[1].to_right=false;
               w = colisionH(var.player[1], -5.0);
               var.player[1].x += w.intValue();
            }
            if(fire2==1 && var.player[1].shot_timer==0 && var.player[1].can_shot){
               var.player[1].shot_timer=var.player[1].weapon.cooldown;
               var.player[1].can_shot=false;
            }
            if(fire2==0 || var.player[1].weapon.automatic){
               var.player[1].can_shot=true;
            }
         }

         for(int i=0;i<var.pl_counter;i++){
            gravity(var.player[i]);
            hit_drop(var.player[i]);
         }
         for(int i=0; i<var.pl_counter; i++){
            if(var.player[i].y<0){
               var.player[i].speed.down=0.0;
               var.player[i].y=0;
            }
         }
         for(int i=0;i<var.drop_counter;i++){
            if(var.drops[i].timer>0){
               var.drops[i].timer--;
            }
         }
         for(int i=0;i<var.bl_counter;i++){
            if(var.bullets[i].show)
            {
               w = bullet_movement(var.bullets[i]);
               if(w==0){
                  var.bullets[i].show=false;
               }
               else
                  var.bullets[i].x+=w.intValue();
               hit(var.bullets[i]);
            }
         }
      }

      { // Shooting
         for(int i=0;i<var.pl_counter;i++){
            if(var.player[i].weapon.exists && var.player[i].shot_timer==var.player[i].weapon.cooldown){
               shot(var.player[i], g);
            }
            if(var.player[i].shot_timer>0){
               var.player[i].shot_timer--;
            }
         }
      }

      { // Information exchange
         try {
            { // conects
               conectionSocket = welcomeSocket.accept(); //Espera a conexão
               conectionSocket2 = welcomeSocket2.accept(); //Espera a conexão

               clientString = new BufferedReader(new InputStreamReader(conectionSocket.getInputStream()));
               clientString2 = new BufferedReader(new InputStreamReader(conectionSocket2.getInputStream()));

               serverToClient = new DataOutputStream(conectionSocket.getOutputStream());
               serverToClient2 = new DataOutputStream(conectionSocket2.getOutputStream());
            }

            { // Recieves information
               clientInput = clientString.readLine();//Le uma linha do clienteInput
               if(clientInput.length()>5){
                  if(!(clientInput.substring(0,5).equals("40404"))){
                     chat.messages.add("1:" + clientInput);
                  }
               }
               else{
                  chat.messages.add("1:" + clientInput);;
               }
               right = clientString.read();
               up = clientString.read();
               left = clientString.read();
               fire = clientString.read();

               clientInput = clientString2.readLine();//Le uma linha do clienteInput
               if(clientInput.length()>5){
                  if(!(clientInput.substring(0,5).equals("40404"))){
                     chat.messages.add("2:" + clientInput);
                  }
               }
               else{
                  chat.messages.add("2:" + clientInput);;
               }
               right2 = clientString2.read();
               up2 = clientString2.read();
               left2 = clientString2.read();
               fire2 = clientString2.read();
            }

            { // Sends information
               { // chat
                  aux = chat.messages.size()- 20;
                  if (aux<0) {
                     aux = 0;
                  }
                  serverToClient.write(chat.messages.size()-aux);

                  for(int i=aux;i<chat.messages.size();i++){
                     serverToClient.writeBytes(chat.messages.get(i) + '\n');
                  }

                  aux = chat.messages.size()- 20;
                  if (aux<0) {
                     aux = 0;
                  }
                  serverToClient2.write(chat.messages.size()-aux);

                  for(int i=aux;i<chat.messages.size();i++){
                     serverToClient2.writeBytes(chat.messages.get(i) + '\n');
                  }
               }

               { // Player info
                  { // client 1
                     if(var.player[0].to_right){
                        serverToClient.write(1);
                     }
                     else{
                        serverToClient.write(0);
                     }
                     nhe = var.player[0].x.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient.write(nhe.intValue());
                     aux = var.player[0].x;
                     aux = aux%100;
                     serverToClient.write(aux);
                     nhe = var.player[0].y.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient.write(nhe.intValue());
                     aux = var.player[0].y;
                     aux = aux%100;
                     serverToClient.write(aux);
                     serverToClient.write(var.player[0].hp);

                     if(var.player[1].to_right){
                        serverToClient.write(1);
                     }
                     else{
                        serverToClient.write(0);
                     }
                     nhe = var.player[1].x.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient.write(nhe.intValue());
                     aux = var.player[1].x;
                     aux = aux%100;
                     serverToClient.write(aux);
                     nhe = var.player[1].y.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient.write(nhe.intValue());
                     aux = var.player[1].y;
                     aux = aux%100;
                     serverToClient.write(aux);
                     serverToClient.write(var.player[1].hp);
                  }

                  { // client 2
                     if(var.player[0].to_right){
                        serverToClient2.write(1);
                     }
                     else{
                        serverToClient2.write(0);
                     }
                     nhe = var.player[0].x.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient2.write(nhe.intValue());
                     aux = var.player[0].x;
                     aux = aux%100;
                     serverToClient2.write(aux);
                     nhe = var.player[0].y.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient2.write(nhe.intValue());
                     aux = var.player[0].y;
                     aux = aux%100;
                     serverToClient2.write(aux);
                     serverToClient2.write(var.player[0].hp);

                     if(var.player[1].to_right){
                        serverToClient2.write(1);
                     }
                     else{
                        serverToClient2.write(0);
                     }
                     nhe = var.player[1].x.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient2.write(nhe.intValue());
                     aux = var.player[1].x;
                     aux = aux%100;
                     serverToClient2.write(aux);
                     nhe = var.player[1].y.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient2.write(nhe.intValue());
                     aux = var.player[1].y;
                     aux = aux%100;
                     serverToClient2.write(aux);
                     serverToClient2.write(var.player[1].hp);
                  }
               }

               { // bullets
                  for(int i=0;i<var.bl_counter;i++){
                     if(var.bullets[i].show){
                        serverToClient.write(1);
                     }
                     else{
                        serverToClient.write(0);
                     }
                     nhe = var.bullets[i].x.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient.write(nhe.intValue());
                     aux = var.bullets[i].x;
                     aux = aux%100;
                     serverToClient.write(aux);
                     nhe = var.bullets[i].y.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient.write(nhe.intValue());
                     aux = var.bullets[i].y;
                     aux = aux%100;
                     serverToClient.write(aux);
                     if(var.bullets[i].speed.right>0){
                        serverToClient.write(1);
                     }
                     else{
                        serverToClient.write(0);
                     }
                  }

                  for(int i=0;i<var.bl_counter;i++){
                     if(var.bullets[i].show){
                        serverToClient2.write(1);
                     }
                     else{
                        serverToClient2.write(0);
                     }
                     nhe = var.bullets[i].x.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient2.write(nhe.intValue());
                     aux = var.bullets[i].x;
                     aux = aux%100;
                     serverToClient2.write(aux);
                     nhe = var.bullets[i].y.doubleValue();
                     nhe=(nhe-nhe%100)/100;
                     serverToClient2.write(nhe.intValue());
                     aux = var.bullets[i].y;
                     aux = aux%100;
                     serverToClient2.write(aux);
                     if(var.bullets[i].speed.right>0){
                        serverToClient2.write(1);
                     }
                     else{
                        serverToClient2.write(0);
                     }
                  }
               }

               { // weapon
                  for(int i=0;i<var.pl_counter;i++){
                     serverToClient.write(var.player[i].weapon.damage);
                  }
                  for(int i=0;i<var.pl_counter;i++){
                     serverToClient2.write(var.player[i].weapon.damage);
                  }
               }

               { // drops
                  for (int i=0; i<var.drop_counter; i++) {
                     serverToClient.write(var.drops[i].timer);
                     serverToClient2.write(var.drops[i].timer);
                  }
               }
            }
         } catch (IOException e) {
            System.out.println("Can't conect");
         }
      }
   }

   public static void main(String argv[]) throws Exception{
      Munhoz_Engine canvas = new Server();
      new Start(canvas);
   }
}
