APCS final project


initially this whole game was supposed to be much more like doodlejump where you moved up the game screen and went through infinitely randomly spawning platforms and faced enemies in varying difficulties. however by the time i had a page full in my composition notebook trying to find an optimized algorithm for procedural generation of platform size, platform length, enemies, etc. such that the game was A) always possible to beat and B) fun, i figured we were already kind of late and should start getting stuff on screen. we then kept building on "getting stuff on screen" until we got where we are now, which is  :

------- 
GAME DETAILS
============
youre a colorful but decidedly aloof character in a bright, sunny world. you can only move left and right (your legs only move too, your arms and head motionless as you shuffle along), and flip your gravity (which is much more kickass than jumping). all of a sudden a cluster of angry dark black red clouds, so angry they vigorously shake to and fro in the air out of anger, so angry theyre willing to chase you down (albeit pretty slowly cause theyre so busy being angry) and TAKE YOUR MONEY OUT OF YOUR WALLET! (which you only have $1k of anyways)! Thankfully you have DEADLY FEET which, on contact with an enemy, takes your money AND THEIRS back (you get a lot more back from enemies than they from you, i assume they scare the dollars into reproducing), kills the enemy (but prompts them to send a last dying message to their cluster, which in turn causes an enemy to spawn anew somewhere else). but if you contact them with anything except your deadly feet, they take away your money! and be careful, because the contrast between the ultra-sachharine world and the evil, angsty clouds greatly confuses our protagonist, limiting his lifespan to just 300 seconds! gain as much money from your enemies as possible before your inevitable death! 

-------

we were planning to add a punching feature but there was not enough time to fully integrate that. 
what we were working on is in the "punch repo". we were also planning to :

- make the platform more intricate (thats why you see a 64x64 in the assets folder as opposed to 16x16 which much better suits the current size)

- clean up a really ugly method in Hero (setSheets for sprite sheet animation breakdown) that was not generalizible at all and relied on hard-coded idiosyncacries. we wanted to make a "SheetHelper" class that took a Texture and an idiosyncacries[][] array and framesize/framewidth parameters (and other general things that made sheet break down much more generalized) and dealt with the texture with those idiosyncacries. again, we figured there was not enough time to expand the game to that point anyways and just made sure that our enemy sprite didnt have animations so we wouldnt have to rewrite that method again.

--------
to SEE CODE and RUN GAME : all the code is in master and the runnable game is GRAVITY_JUMP.jar.

to BUILD GAME : go to the build branch and run "sudo ./gradlew desktop:run", assuming youre on -nix. dont know what the equiv command is for windows but it probaaably has something to do with gradlew.bat. 

to build the jar yourself : "sudo ./gradlew desktop:dist" then the jar is in desktop/build/libs/desktop-1.0.jar

