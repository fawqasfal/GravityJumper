GravityJumper
=============

APCS final project

things we didnt finish because we were already really late :

initially this whole game was supposed to be much more like doodlejump where you moved up the game screen and went through
infinitely randomly spawning platforms and faced enemies in varying difficulties. however by the time i had a page full in my
composition notebook trying to find an optimized algorithm for procedural generation of platform size, platform length, enemies, etc. such that the game was A) always possible to beat and B) fun, i figured we were already kind of late and should start getting stuff on screen. we then kept building on "getting stuff on screen" until we got where we are now.

we were planning to add a punching feature but there was not enough time to fully integrate that.
what we were working on is in the punch repo.
we were also planning to :

- make the platform more intricate (thats why you see a 64x64 in the assets folder as opposed to 16x16 which much better suits the current size)

- clean up a really ugly method in Hero (setSheets for sprite sheet animation breakdown) that was not generalizible at all and relied on hard-coded idiosyncacries. we wanted to make a "SheetHelper" class that took a Texture and an idiosyncacries[][] array and dealt with the texture with those idiosyncacries. again, we figured there was not enough time to expand the game to that point anyways and just made sure that our enemy sprite didnt have animations so we wouldnt have to rewrite that method again.

-
to SEE CODE and RUN GAME :
  all the code is in master and the runnable game is GRAVITY_JUMP.jar.

to BUILD GAME :
  stay on the build branch and run
    sudo ./gradlew desktop:run
  to build the jar yourself :
    sudo ./gradlew desktop:dist
  then the jar is in
    desktop/build/libs/desktop-1.0.jar


i have 3k+ added lines in master because i accidentally commited a bunch of build shit to master that was not code at all.
that is NOT representative of the code amt we commited. the commit amounts are a bit more realisitic, 43% for shein and 57% for me.
