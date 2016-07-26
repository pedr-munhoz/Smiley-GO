import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.imageio.*;


class Client extends Munhoz_Engine {
   Basic_Variables var = new Basic_Variables();

   Keyboard k = new Keyboard();
   Chat chat = new Chat();
   Character aux;
   int x, right, left, up, fire;
   String InputInfo;
   String OutputInfo;
   Image backgroung_img;
   Integer zatd;

   BufferedReader entry = new BufferedReader(new InputStreamReader(System.in));
   int port = 0;
   Socket clientSocket;
   DataOutputStream clientToServer;
   BufferedReader serverString;

   public void init(){
      String aux = new String();
      chat.next_message=" ";
      setSize(1300, 690);

      for(int i=0;i<var.pl_counter;i++){
         try {
            var.player[i].right = ImageIO.read(new File("assets/blank_1_r.gif"));
            var.player[i].height = var.player[i].right.getHeight(this);
            var.player[i].width = var.player[i].right.getWidth(this);
         } catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }
         try {
            var.player[i].left = ImageIO.read(new File("assets/blank_1_l.gif"));
         } catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }
         var.player[i].weapon = new Weapon();
         var.player[i].weapon.width = 29;
         var.player[i].weapon.right = var.revolver_r;
         var.player[i].weapon.left = var.revolver_l;
         var.player[i].weapon.x=25;
         var.player[i].weapon.y=25;
      }

      { // Image IO
         try {
            var.assault_rifle_r = ImageIO.read(new File("assets/assault_rifle_right.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }
         try {
            var.assault_rifle_l = ImageIO.read(new File("assets/assault_rifle_left.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }

         try {
            var.rifle_r = ImageIO.read(new File("assets/rifle_right.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }
         try {
            var.rifle_l = ImageIO.read(new File("assets/rifle_left.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }

         try {
            var.revolver_r = ImageIO.read(new File("assets/revolver_right.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }
         try {
            var.revolver_l = ImageIO.read(new File("assets/revolver_left.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }

         try {
            var.rocket_louncher_r = ImageIO.read(new File("assets/rocket_louncher.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }
         try {
            var.rocket_louncher_l = ImageIO.read(new File("assets/rocket_louncher.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }

         try {
            var.bullet_r = ImageIO.read(new File("assets/bulletR.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }
         try {
            var.bullet_l = ImageIO.read(new File("assets/bulletL.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }

         try {
            var.explosion_img = ImageIO.read(new File("assets/explosion.gif"));
         }
         catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }

         for(int j=0;j<4;j++){
            try {
               var.player[0].hp_bar[j] = ImageIO.read(new File("assets/hp"+(j+1)+".png"));
            } catch (IOException e) {
               System.out.println("A imagem nao pode ser carregada.");
            }
         }

         /*try {
            backgroung = ImageIO.read(new File(""));
         } catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }*/
      }

      for (int i=0; i<var.bl_counter; i++) {
         var.bullets[i].r = var.bullet_r;
         var.bullets[i].l = var.bullet_l;
         var.bullets[i].height=var.bullets[i].r.getHeight(this);
         var.bullets[i].width=var.bullets[i].r.getWidth(this);
      }

      { // user entry port number
         try {
            port = zatd.decode(entry.readLine());
         } catch (IOException e) {
            System.out.println("nhe");
         }
      }

      setObstacles();
      setDrops();

      var.player[0].x = 50;
      var.player[1].x = 1200;
      var.player[0].y = 50;
      var.player[1].y = 50;
      var.player[0].hp = 4;
      var.player[1].hp = 4;

      //port = 9090;
   }

   void setObstacles(){
      var.obstacles[0]=new Obstacle((window.width-900)/2,window.height-50,"assets/p900.png");
      var.obstacles[1]=new Obstacle((window.width-450)/2,window.height-170,"assets/p450.png");
      var.obstacles[2]=new Obstacle(0,window.height-170,"assets/p200.png");
      var.obstacles[3]=new Obstacle(window.width-200,window.height-170,"assets/p200.png");
      var.obstacles[4]=new Obstacle(0,window.height-620,"assets/pv450.png");
      var.obstacles[5]=new Obstacle(window.width-14,window.height-620,"assets/pv450.png");
      var.obstacles[6]=new Obstacle(130,window.height-290,"assets/p350.png");
      var.obstacles[7]=new Obstacle(window.width-480,window.height-290,"assets/p350.png");
      var.obstacles[8]=new Obstacle(220,window.height-410,"assets/pv120.png");
      var.obstacles[9]=new Obstacle(window.width-234,window.height-410,"assets/pv120.png");
      var.obstacles[10]=new Obstacle(150,window.height-410,"assets/p120.png");
      var.obstacles[11]=new Obstacle(window.width-280,window.height-410,"assets/p120.png");
      var.obstacles[12]=new Obstacle((window.width-200)/2,window.height-410,"assets/p200.png");
      var.obstacles[13]=new Obstacle(((window.width-200)/2-270)/2+270,window.height-530,"assets/pv120.png");
      var.obstacles[14]=new Obstacle(window.width-((window.width-200)/2-270)/2-270,window.height-530,"assets/pv120.png");
      var.obstacles[15]=new Obstacle(14,window.height-530,"assets/p450.png");
      var.obstacles[16]=new Obstacle(window.width-450-14,window.height-530,"assets/p450.png");
   }

   void setDrops(){
      var.drops[0]=new Weapon_Drop(window.width/2,window.height-50,"rocket_louncher",var);
      var.drops[1]=new Weapon_Drop(window.width/2,window.height-170,"rifle",var);
      var.drops[2]=new Weapon_Drop(window.width/2,window.height-410,"assault_rifle",var);
      var.drops[3]=new Weapon_Drop(50,window.height-170,"revolver",var);
      var.drops[4]=new Weapon_Drop(window.width-50,window.height-170,"revolver",var);
      var.drops[5]=new Weapon_Drop(210,window.height-410,"revolver",var);
      var.drops[6]=new Weapon_Drop(window.width-210,window.height-410,"revolver",var);
      var.drops[7]=new Weapon_Drop(300,window.height-530,"revolver",var);
      var.drops[8]=new Weapon_Drop(window.width-300,window.height-530,"revolver",var);

      for(int i=0;i<var.drop_counter;i++){
         var.drops[i].timer=0;
      }
   }

   public void paint(Graphics g) {
      { // Moviment
         up=0;
         right=0;
         left=0;
         fire=0;
         if(keyboard.keys.contains(38)){
            up=1;
         }
         if(keyboard.keys.contains(39)){
            right=1;
         }
         else if(keyboard.keys.contains(37)){
            left=1;
         }
         if(keyboard.keys.contains(112)){
            fire=1;
         }
      }

      { // Information exchange
         chat.messages.clear();

         if(keyboard.keys.size()>0){
            if(keyboard.keys.get(keyboard.keys.size()-1)=='\n'){
               if (!chat.k_count.contains(27)) {
                  try {
                     clientSocket = new Socket("localhost", port);
                     clientToServer = new DataOutputStream(clientSocket.getOutputStream());
                     BufferedReader serverString = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                     { // sends information
                        { // chat
                           clientToServer.writeBytes(chat.next_message + '\n');
                           chat.next_message=" ";
                        }

                        { // controls
                           clientToServer.write(right);
                           clientToServer.write(up);
                           clientToServer.write(left);
                           clientToServer.write(fire);
                        }
                     }

                     { // recieves information
                        { // chat
                           x = serverString.read();
                           for (int i =0; i<x; i++) {
                              chat.messages.add(serverString.readLine());
                           }
                        }

                        { // player
                           for (int i=0; i<var.pl_counter; i++) {
                              x = serverString.read();
                              var.player[i].to_right = (x==1);
                              var.player[i].x = serverString.read() * 100;
                              var.player[i].x += serverString.read();
                              var.player[i].y = serverString.read() * 100;
                              var.player[i].y += serverString.read();
                              var.player[i].hp = serverString.read();
                           }
                        }

                        { // bullets
                           for(int i=0;i<var.bl_counter;i++){
                              x = serverString.read();
                              if(x==1){
                                 var.bullets[i].show = true;
                              }
                              else{
                                 var.bullets[i].show = false;
                              }
                              var.bullets[i].x = serverString.read() * 100;
                              x = serverString.read();
                              var.bullets[i].x += x;
                              var.bullets[i].y = serverString.read() * 100;
                              x = serverString.read();
                              var.bullets[i].y += x;
                              x = serverString.read();
                              if(x==1){
                                 var.bullets[i].image = var.bullets[i].r;
                              }
                              else{
                                 var.bullets[i].image = var.bullets[i].l;
                              }
                           }
                        }

                        { // weapons
                           for(int i=0;i<var.pl_counter;i++){
                              x = serverString.read();
                              switch(x){
                                case 1:
                                var.player[i].weapon.width = 48;
                                var.player[i].weapon.right = var.assault_rifle_r;
                                var.player[i].weapon.left = var.assault_rifle_l;
                                var.player[i].weapon.x=20;
                                var.player[i].weapon.y=25;
                                break;
                                case 3:
                                var.player[i].weapon.width = 29;
                                var.player[i].weapon.right = var.revolver_r;
                                var.player[i].weapon.left = var.revolver_l;
                                var.player[i].weapon.x=25;
                                var.player[i].weapon.y=25;
                                break;
                                case 4:
                                var.player[i].weapon.width = 67;
                                var.player[i].weapon.right = var.rifle_r;
                                var.player[i].weapon.left = var.rifle_l;
                                var.player[i].weapon.x=10;
                                var.player[i].weapon.y=25;
                                break;
                                case 40:
                                var.player[i].weapon.width = 66;
                                var.player[i].weapon.right = var.rocket_louncher_r;
                                var.player[i].weapon.left = var.rocket_louncher_l;
                                var.player[i].weapon.x=0;
                                var.player[i].weapon.y=5;
                              }
                           }
                        }

                        { // drops
                           for (int i=0; i<var.drop_counter; i++) {
                              var.drops[i].timer = serverString.read();
                           }
                        }
                     }
                  } catch (IOException e) {
                     System.out.println("Host not avaliable");
                  }
                  chat.k_count.add(27);
               }
               else {
                  try {
                     clientSocket = new Socket("localhost", port);
                     clientToServer = new DataOutputStream(clientSocket.getOutputStream());
                     BufferedReader serverString = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                     { // sends information
                        { // chat
                           clientToServer.writeBytes("404040" + '\n');
                        }

                        { // controls
                           clientToServer.write(right);
                           clientToServer.write(up);
                           clientToServer.write(left);
                           clientToServer.write(fire);
                        }
                     }

                     { // recieves information
                        { // chat
                           x = serverString.read();
                           for (int i =0; i<x; i++) {
                              chat.messages.add(serverString.readLine());
                           }
                        }

                        { // player
                           for (int i=0; i<var.pl_counter; i++) {
                              x = serverString.read();
                              var.player[i].to_right = (x==1);
                              var.player[i].x = serverString.read() * 100;
                              var.player[i].x += serverString.read();
                              var.player[i].y = serverString.read() * 100;
                              var.player[i].y += serverString.read();
                              var.player[i].hp = serverString.read();
                           }
                        }

                        { // bullets
                           for(int i=0;i<var.bl_counter;i++){
                              x = serverString.read();
                              if(x==1){
                                 var.bullets[i].show = true;
                              }
                              else{
                                 var.bullets[i].show = false;
                              }
                              var.bullets[i].x = serverString.read() * 100;
                              x = serverString.read();
                              var.bullets[i].x += x;
                              var.bullets[i].y = serverString.read() * 100;
                              x = serverString.read();
                              var.bullets[i].y += x;
                              x = serverString.read();
                              if(x==1){
                                 var.bullets[i].image = var.bullets[i].r;
                              }
                              else{
                                 var.bullets[i].image = var.bullets[i].l;
                              }
                           }
                        }

                        { // weapons
                           for(int i=0;i<var.pl_counter;i++){
                              x = serverString.read();
                              switch(x){
                                case 1:
                                var.player[i].weapon.width = 48;
                                var.player[i].weapon.right = var.assault_rifle_r;
                                var.player[i].weapon.left = var.assault_rifle_l;
                                var.player[i].weapon.x=20;
                                var.player[i].weapon.y=25;
                                break;
                                case 3:
                                var.player[i].weapon.width = 29;
                                var.player[i].weapon.right = var.revolver_r;
                                var.player[i].weapon.left = var.revolver_l;
                                var.player[i].weapon.x=25;
                                var.player[i].weapon.y=25;
                                break;
                                case 4:
                                var.player[i].weapon.width = 67;
                                var.player[i].weapon.right = var.rifle_r;
                                var.player[i].weapon.left = var.rifle_l;
                                var.player[i].weapon.x=10;
                                var.player[i].weapon.y=25;
                                break;
                                case 40:
                                var.player[i].weapon.width = 66;
                                var.player[i].weapon.right = var.rocket_louncher_r;
                                var.player[i].weapon.left = var.rocket_louncher_l;
                                var.player[i].weapon.x=0;
                                var.player[i].weapon.y=5;
                              }
                           }
                        }

                        { // drops
                           for (int i=0; i<var.drop_counter; i++) {
                              var.drops[i].timer = serverString.read();
                           }
                        }
                     }
                  } catch (IOException e) {
                     System.out.println("Host not avaliable");
                  }
               }
            }
            else {
               try {
                  clientSocket = new Socket("localhost", port);
                  clientToServer = new DataOutputStream(clientSocket.getOutputStream());
                  BufferedReader serverString = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                  { // sends information
                     { // chat
                        clientToServer.writeBytes("404040" + '\n');
                     }

                     { // controls
                        clientToServer.write(right);
                        clientToServer.write(up);
                        clientToServer.write(left);
                        clientToServer.write(fire);
                     }
                  }

                  { // recieves information
                     { // chat
                        x = serverString.read();
                        for (int i =0; i<x; i++) {
                           chat.messages.add(serverString.readLine());
                        }
                     }

                     { // player
                        for (int i=0; i<var.pl_counter; i++) {
                           x = serverString.read();
                           var.player[i].to_right = (x==1);
                           var.player[i].x = serverString.read() * 100;
                           var.player[i].x += serverString.read();
                           var.player[i].y = serverString.read() * 100;
                           var.player[i].y += serverString.read();
                           var.player[i].hp = serverString.read();
                        }
                     }

                     { // bullets
                        for(int i=0;i<var.bl_counter;i++){
                           x = serverString.read();
                           if(x==1){
                              var.bullets[i].show = true;
                           }
                           else{
                              var.bullets[i].show = false;
                           }
                           var.bullets[i].x = serverString.read() * 100;
                           x = serverString.read();
                           var.bullets[i].x += x;
                           var.bullets[i].y = serverString.read() * 100;
                           x = serverString.read();
                           var.bullets[i].y += x;
                           x = serverString.read();
                           if(x==1){
                              var.bullets[i].image = var.bullets[i].r;
                           }
                           else{
                              var.bullets[i].image = var.bullets[i].l;
                           }
                        }
                     }

                     { // weapons
                        for(int i=0;i<var.pl_counter;i++){
                           x = serverString.read();
                           switch(x){
                             case 1:
                             var.player[i].weapon.width = 48;
                             var.player[i].weapon.right = var.assault_rifle_r;
                             var.player[i].weapon.left = var.assault_rifle_l;
                             var.player[i].weapon.x=20;
                             var.player[i].weapon.y=25;
                             break;
                             case 3:
                             var.player[i].weapon.width = 29;
                             var.player[i].weapon.right = var.revolver_r;
                             var.player[i].weapon.left = var.revolver_l;
                             var.player[i].weapon.x=25;
                             var.player[i].weapon.y=25;
                             break;
                             case 4:
                             var.player[i].weapon.width = 67;
                             var.player[i].weapon.right = var.rifle_r;
                             var.player[i].weapon.left = var.rifle_l;
                             var.player[i].weapon.x=10;
                             var.player[i].weapon.y=25;
                             break;
                             case 40:
                             var.player[i].weapon.width = 66;
                             var.player[i].weapon.right = var.rocket_louncher_r;
                             var.player[i].weapon.left = var.rocket_louncher_l;
                             var.player[i].weapon.x=0;
                             var.player[i].weapon.y=5;
                           }
                        }
                     }

                     { // drops
                        for (int i=0; i<var.drop_counter; i++) {
                           var.drops[i].timer = serverString.read();
                        }
                     }
                  }
               } catch (IOException e) {
                  System.out.println("Host not avaliable");
               }
               int y = chat.k_count.indexOf(27);
               if(y!=-1){
                  chat.k_count.remove(y);
               }
            }
         }

         else {
            try {
               clientSocket = new Socket("localhost", port);
               clientToServer = new DataOutputStream(clientSocket.getOutputStream());
               BufferedReader serverString = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

               { // sends information
                  { // chat
                     clientToServer.writeBytes("404040" + '\n');
                  }

                  { // controls
                     clientToServer.write(right);
                     clientToServer.write(up);
                     clientToServer.write(left);
                     clientToServer.write(fire);
                  }
               }

               { // recieves information
                  { // chat
                     x = serverString.read();
                     for (int i =0; i<x; i++) {
                        chat.messages.add(serverString.readLine());
                     }
                  }

                  { // player
                     for (int i=0; i<var.pl_counter; i++) {
                        x = serverString.read();
                        var.player[i].to_right = (x==1);
                        var.player[i].x = serverString.read() * 100;
                        var.player[i].x += serverString.read();
                        var.player[i].y = serverString.read() * 100;
                        var.player[i].y += serverString.read();
                        var.player[i].hp = serverString.read();
                     }
                  }

                  { // bullets
                     for(int i=0;i<var.bl_counter;i++){
                        x = serverString.read();
                        if(x==1){
                           var.bullets[i].show = true;
                        }
                        else{
                           var.bullets[i].show = false;
                        }
                        var.bullets[i].x = serverString.read() * 100;
                        x = serverString.read();
                        var.bullets[i].x += x;
                        var.bullets[i].y = serverString.read() * 100;
                        x = serverString.read();
                        var.bullets[i].y += x;
                        x = serverString.read();
                        if(x==1){
                           var.bullets[i].image = var.bullets[i].r;
                        }
                        else{
                           var.bullets[i].image = var.bullets[i].l;
                        }
                     }
                  }

                  { // weapons
                     for(int i=0;i<var.pl_counter;i++){
                        x = serverString.read();
                        switch(x){
                          case 1:
                          var.player[i].weapon.width = 48;
                          var.player[i].weapon.right = var.assault_rifle_r;
                          var.player[i].weapon.left = var.assault_rifle_l;
                          var.player[i].weapon.x=20;
                          var.player[i].weapon.y=25;
                          break;
                          case 3:
                          var.player[i].weapon.width = 29;
                          var.player[i].weapon.right = var.revolver_r;
                          var.player[i].weapon.left = var.revolver_l;
                          var.player[i].weapon.x=25;
                          var.player[i].weapon.y=25;
                          break;
                          case 4:
                          var.player[i].weapon.width = 67;
                          var.player[i].weapon.right = var.rifle_r;
                          var.player[i].weapon.left = var.rifle_l;
                          var.player[i].weapon.x=10;
                          var.player[i].weapon.y=25;
                          break;
                          case 40:
                          var.player[i].weapon.width = 66;
                          var.player[i].weapon.right = var.rocket_louncher_r;
                          var.player[i].weapon.left = var.rocket_louncher_l;
                          var.player[i].weapon.x=0;
                          var.player[i].weapon.y=5;
                        }
                     }
                  }

                  { // drops
                     for (int i=0; i<var.drop_counter; i++) {
                        var.drops[i].timer = serverString.read();
                     }
                  }
               }
            } catch (IOException e) {
               System.out.println("Host not avaliable");
            }
            int y = chat.k_count.indexOf(27);
            if(y!=-1){
               chat.k_count.remove(y);
            }
         }
         g.drawString("    "+chat.next_message,0,550);
      }

      { // Display graphic elements
         { // chat
            for (int i =chat.messages.size()-1; i>=0; i--) {
               g.drawString("    "+chat.messages.get(i),30,10+20*(i));
            }
         }

         { // weapon drops
            for(int i=0;i<var.drop_counter;i++){
               if(var.drops[i].timer==0){
                  g.drawImage(var.drops[i].weapon.right,var.drops[i].x-var.drops[i].width/2,var.drops[i].y-var.drops[i].height,this);
               }
            }
         }

         { // obstacles
            for(int i=0;i<var.obs_counter;i++){
               g.drawImage(var.obstacles[i].image,var.obstacles[i].x,var.obstacles[i].y,this);
            }
         }

         { // player
            for(int i=0;i<var.pl_counter;i++){
               g.drawImage(var.player[i].hp_bar[var.player[i].hp-1],var.player[i].x, var.player[i].y-20, this);
               if(var.player[i].to_right){
                  g.drawImage(var.player[i].right, var.player[i].x, var.player[i].y, this);
                  g.drawImage(var.player[i].weapon.right,var.player[i].x+var.player[i].weapon.x,var.player[i].y+var.player[i].weapon.y, this);
               }
               else{
                  g.drawImage(var.player[i].left, var.player[i].x, var.player[i].y, this);
                  g.drawImage(var.player[i].weapon.left,var.player[i].x-var.player[i].weapon.width+var.player[i].width-var.player[i].weapon.x,var.player[i].y+var.player[i].weapon.y, this);
               }
            }
         }

         { // bullets
            for(int i=0;i<var.bl_counter;i++){
               if(var.bullets[i].show){
                  g.drawImage(var.bullets[i].image,var.bullets[i].x,var.bullets[i].y,this);
               }
            }
         }

         { // chat
            for (int i=32; i<=127; i++) {
               if(keyboard.keys.contains(i)){
                  if(!chat.k_count.contains(i) && !(i>36&&i<40) && i!=112){
                     chat.next_message = chat.next_message + aux.toString(keyboard.keys_c.get(keyboard.keys.indexOf(i)));
                     chat.k_count.add(i);
                  }
               }
               else{
                  int y = chat.k_count.indexOf(i);
                  if(y!=-1){
                     chat.k_count.remove(y);
                  }
               }
            }

            if(keyboard.keys.contains(8)){
               if(!chat.k_count.contains(8)&&chat.next_message.length()>0){
                  chat.next_message = chat.next_message.substring(0,chat.next_message.length()-1);
                  chat.k_count.add(8);
               }
            }
            else{
               int y = chat.k_count.indexOf(8);
               if(y!=-1){
                  chat.k_count.remove(y);
               }
            }
         }
      }
   }

   public static void main(String args[]){
      Munhoz_Engine canvas = new Client();
      new Start(canvas);
   }

}
