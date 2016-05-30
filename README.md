# Distributed-Systems-Assignment
Socket programming, RMI, multithreading

A repo két egymásra épülő egyetemi beadandó kidolgozását tartalmazza.

##Az első beadandó

###A feladat összefoglaló leírása
A klasszikus szólánc játék során a 2 játékos felváltva mond 1-1 szót úgy, hogy a következő szónak mindig az előző szó utolsó betűjével kell kezdődnie. Készíts egy szervert, ami biztosítja a játékos párok közötti kommunikációt és nyomon követi a játékmeneteket! Kliensként kétféle játékost valósítunk meg: az automata játékost, amelynek a szókincsét egy megadott fájl tartalmazza, és ez alapján játszik; valamint egy interaktív konzolos felülettel rendelkező klienst, ahol a felhasználó által begépelt szavakat továbbítjuk a másik játékos felé.

###A játékszerver
A játékszervert a 32123 porthoz rendeljük. A szerver egy játékmenetet a következőképpen bonyolít le:

Várakozik két játékos csatlakozására, akik a csatlakozás után megküldik a szervernek nevüket.

A szerver létrehoz egy fájlt, amibe játékmenet során összeálló szóláncot fogja rögzíteni, a következő névvel: <jatekos1>_<jatekos2>_<idobelyeg>.txt

Amint a második játékos is csatlakozott, egy speciális start üzenettel jelzi az először csatlakozottnak, hogy ő a kezdőjátékos, tehát először neki kell egy tetszőleges szót mondania.

A szerver innentől kezdve mindig fogad egy egy szavas üzenetet az egyik játékostól, majd (ellenőrzés nélkül) továbbítja a másik játékos felé, aki válaszként elküldi a szólánc következő elemét, amit a szerver továbbít, stb.

A szerver rögzíti a játék során összeálló szóláncot a játékmenethez létrehozott fájlba. Egy sorban a beküldő játékos neve, majd attól szóközzel elválasztva az általa beküldött szó szerepeljen.

Ha valamelyik játékos az exit üzenetet küldi (ez a szóláncban tiltott szó lesz), vagy váratlanul lecsatlakozik, a játékmenet véget ér, és a másik játékos nyert. A nyertest a szerver a nyert üzenettel értesítse, majd mindkét játékossal bontsa a kapcsolatot.

A szervert készítsük fel több játékmenet egy időben történő kezelésére: tehát minden két, egymás után csatlakozott játékoshoz indítson el egy játékmenetet, majd azonnal legyen képes újabb két játékos fogadására. A szerver álljon le, ha 30 másodpercen keresztül nem csatlakozik egy játékos sem (tipp: ServerSocket osztály setSoTimout metódusa), és már nincsen folyamatban lévő játék.

###Gépi játékos
Készítsünk egy főosztályt a gépi játékosoknak. A program első parancssori argumentuma a játékos neve, második pedig egy fájlnév. Ebben a szöveges fájlban található a játékos szókincse: soronként egy-egy szó. Csatlakozzon a gépi játékos a szerverhez és küldje el a nevét. A szervertől kapott első üzenetben vagy a kezdőjátékosnak szóló speciális start szó lesz, vagy pedig a szólánc első szava.

Amennyiben a start üzenet érkezett, a gépi játékos válassza ki és küldje el a szókincse legelső szavát (a szavak sorrendje az a sorrend, ahogyan a fájlban szerepeltek).

Ha már a szólánc első szava érkezett, akkor válassza ki és küldje el a szókincséből azt a legelső szót, ami a kapott szó utolsó betűjével kezdődik. A többi lépésben is ugyanígy küldjön válaszüzenetet. Fontos, hogy egy szót egy játékmenet során csak egyszer küldhet el, tehát a már elküldött szót érdemes eltávolítani a szókincsből! Minden elküldött szónál a standard outputra írja ki a következő szöveget: "<név>: <küldött_szó>"

Ha a gépi játékosnak már nincs a leírt szabály szerint küldhető szava, küldje el az exit üzenetet a szervernek, majd fejezze be működését.

Ha a gépi játékos még játékban van, és a nyerést jelző nyert üzenetet kapja, a standard outputra írja ki a következő szöveget: "<név> nyert", majd fejezze be a működését.

###Játékszimuláció
Készíts egy SzolancSzimulacio nevű főosztályt, amely elindít egy játékszervert és négy játékost. A játékosok neve legyen "Jatekos1", "Jatekos2", stb. Az első három játékos szókincse legyen a szokincs1.txt fájl, míg a negyediké a szokincs2.txt. A szókincseket tartalmazó fájlok és a minta kimeneti fájlok letölthetők innen.

###Interaktív kliens
Készíts egy konzolos klienst! A kliens csatlakozzon a játékszervehez, majd kérjen be a felhasználótól egy játékos nevet, amit elküld a szervernek.

Amennyiben a start üzenet érkezett elsőként a szervertől, kérjen be egy tetszőleges szót a felhasználótól, amit továbbít a szerver felé.

Ha már a szólánc első szava érkezett, írja ki a kapott szót a felhasználónak, és kérje be a szólánc következő elemét. A kliensprogram ellenőrizze, hogy tényleg a kapott szó utolsó betűjével kezdődő szót gépelt-e be a felhasználó, ha nem, kérjen be egy újabb szót. Ha rendben van a begépelt szó, továbbítsa a szerver felé.

A felhasználó akármelyik lépésben begépelheti az "exit" szót, ekkor a kliensprogram ezt továbbítsa a szerver felé, majd fejeződjön be a kliens futása.

Ha valamelyik körben a szervertől a nyerést jelző nyert üzenet érkezett, akkor írja ki a felhasználónak, hogy ő nyert, majd fejeződjön be a kliens program futása.

A szólánc helyességén kívül a kliensprogram ellenőrizze minden lépésben azt is, hogy a küldendő String tényleg egyetlen szó-e (csak betűket tartalmaz), és nem küldtük már korábban ugyanezt a szót!

###Főprogram egy gépi és egy interaktív klienssel
Készíts egy SzolancJatek nevű főosztályt, amely lehetővé teszi a gépi játékos ellen való játékot: elindít egy szervert; egy "Robot" nevű, szokincs1.txt szókinccsel rendelkező gépi játékost; és egy interaktív klienst. A játék addig megy, amíg a robot már nem tud megfelelő szót küldeni, vagy a felhasználó feladja a játékot.

##A második beadandó

###A tiltott szavak szervere
Készíts egy TiltottDeploy nevű főosztályt, amelynek főprogramja először elindítja az RMI névszolgáltatást az 12345-ös porton, majd a parancssori paraméter értékének megfelelő számú TiltottSzerver objektumok indít el, amelyeket tiltott1, tiltott2 stb. neveken be is jegyez az RMI névszolgáltatásba (ellenőrizni kell, hogy pontosan egy parancssori paraméter legyen, amelynek értéke legalább egy). A TiltottSzerver objektumok induláskor beolvassák a hozzájuk tartozó szöveges fájlok tartalmát (amely soronként pontosan egy szót tartalmaz), és ezeket a szavakat el is tárolják (a tiltott1 néven bejegyzett objektumhoz a tiltott1.txt fájl tartozik, a tiltott2 objektumhoz a tiltott2.txt, stb.). A TiltottSzerver osztálynak egyetlen távolról is hívható metódusa legyen, amely jelenjen meg a hozzá tartozó interfészben is: public boolean tiltottE(String szo), amely metódus visszatér azzal, hogy a szó szerepelt-e már az adott objektum listájában (amely kezdetben megegyezik a hozzátartozó fájlból beolvasott szavakkal), és egyúttal el is tárolja a paraméterként kapott szót (ezért ugyanazzal a szóval kétszer meghívva, legkésőbb a második hívásnál már mindenképpen igazat ad vissza).

###Módosítások a korábbi feladathoz képest:
###Játékszerver
A játékszerver minden indított játékhoz számontartja, hogy melyik tiltott szavas szerver tartozik hozzá. Az elsőként indított játékhoz a tiltott1, a másodikként indított játékhoz a tiltott2, stb. Ha a szerver az adott számmal már nem talál objektumot az RMI névszolgáltatásban, akkor automatikusan előről kezdi a számozást (ha például a TiltottDeploy osztály 2 objektumot indított, akkor a szerver a harmadik játékhoz ismét a tiltott1 objektumot rendeli, a negyedik játékhoz a tiltott2-t, az ötödik játékhoz ismét a tiltott1-et, stb.). Minden alkalommal, amikor szó érkezik be valamelyik klienstől, a játékszerver meghívja a hozzá tartozó tiltott szavas szerver tiltottE műveletét. Ha a metódus igaz értékkel tér vissza, akkor a szerver az adott kliensnek nok üzenetet küld, és újra az adott klienstől vár szót. Ha a metódus hamis értékkel tért vissza, akkor a küldő klienst ok üzenettel értesíti, majd a játék megy tovább (megküldi a szót a másik kliensnek, majd várakozik a válaszára).

###Gépi kliens
Mivel a szerver minden megkapott szó után ok vagy nok üzenetet küld, ezért a gépi klienst módosítani kell úgy, hogy ezt kezelje. Ha nok üzenetet kap, akkor a szótárából egy megfelelő kezdőbetűjű másik szót kell kiválasztania.

###Interaktív kliens
Szintén kezelnie kell a szerver válaszait: ha a szerver egy adott szóra nok üzenetet küld, akkor ismét egy megfelelő kezdőbetűs szót kell bekérnie a billenytűzetről. A kliensnek a szóismétlést már nem kell ellenőriznie (ezt megteszi helyette a szerver a tiltott szavas objektumokkal), de azt továbbra is ellenőrizze, hogy a szó csak betűkből áll-e.

###Szóláncjáték
A szerver indítása előtt indítsa el a TiltottDeploy programot 1-es parancssori paraméterrel.

###Szóláncszimuláció
A szerver indítása előtt indítsa el a TiltottDeploy programot 2-es parancssori paraméterrel.
