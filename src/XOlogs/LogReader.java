package XOlogs;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import javax.swing.JTextArea;

import static java.util.concurrent.TimeUnit.SECONDS;

public class LogReader extends Thread {
    private final String NO_TEAM = "00000000";
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final SimpleDictionary dictionary = new SimpleDictionary();
    private final int numberWeapons=2;
    ScheduledFuture<?> scheduledFuture;
    //static private AtomicInteger saveBoolean = new AtomicInteger(0);

    private void saveToFile() {
        final Runnable beeper = () -> {
            if (playersListFull != null && !playersListFull.isEmpty()) {
                ArrayList<PlayerShort> tempListA = new ArrayList<>();
                int teamNumber = playersListFull.get(0).getTeamNumber();
                for (PlayerShort p : playersListFull) {
                    if (p.getTeamNumber().equals(teamNumber)) {
                        tempListA.add(p);
                    }
                }

                ArrayList<PlayerShort> tempListB = new ArrayList<>();
                int teamNumber2 = 3 - tempListA.get(0).getTeamNumber();
                for (PlayerShort p : playersListFull) {
                    if (p.getTeamNumber().equals(teamNumber2)) {
                        tempListB.add(p);
                    }
                }
                Comparator <PlayerShort> cmp = (o1, o2) -> {
                    int difference = o1.getWhiteDamage()-o2.getWhiteDamage();
                    if (difference!=0)
                        return o1.getWhiteDamage()-o2.getWhiteDamage();
                    else
                        return o1.compareTo(o2);
                };
                Collections.sort(tempListA,cmp);
                Collections.sort(tempListB,cmp);
                String formString1 = getFormattedTeamList(tempListA,false,numberWeapons);
                String formString2 = getFormattedTeamList(tempListB,true,numberWeapons);
                saveToFile(formString1, "./teamA.txt");
                saveToFile(formString2, "./teamB.txt");
            }
        };

        scheduledFuture = scheduler.scheduleWithFixedDelay(() -> beeper.run(), 5, 2, SECONDS);

    }


    private String St = " ";
    private Integer teamNumber = 1;
    private String myName = " ";
    private Integer InfoAboutTeams = 0;
    private JTextArea textA = null;
    private Integer typeOfFile = 0;
    public void SetType(Integer tp) {
        typeOfFile = tp;
    }
    private ArrayList<Player> Players = null;
    private List<PlayerShort> PList ;
    private ArrayList<PlayerShort> playersListFull;
    public void SetPlayers(ArrayList<Player> p) {
        Players = p;
    }
    public void SetMyName(String Nm) {
        myName = Nm;
    }
    public String getMyName() {
        return myName;
    }

    public void printCommand(String S) {
        boolean doScroll = true;
        if (textA != null) {
            textA.append(S + "\n");
            if (doScroll)
                textA.setCaretPosition(textA.getDocument().getLength() - 1);
        }
    }

    public LogReader() {
        PList = Collections.synchronizedList(new ArrayList<>());
        playersListFull = new ArrayList<>();
        saveToFile();
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    private class PlayerShort implements Comparable<PlayerShort> {
        static final String ORDER = "0123456789_aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ";
        String name;
        Integer teamNumber;
        String teamName;
        Integer score;
        Integer yellowDamage;
        Integer whiteDamage;
        WeaponList myWeapon;

        public Integer getYellowDamage() {
            return yellowDamage;
        }

        public void setYellowDamage(Integer yellowDamage) {
            this.yellowDamage = yellowDamage;
        }

        public Integer getScore() {
            return score;
        }


        public Integer getWhiteDamage() {
            return whiteDamage;
        }

        public String getName() {
            return name;
        }

        public Integer getTeamNumber() {
            return teamNumber;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setName(String Name) {
            name = Name;
        }

        public void setTeamNumber(Integer TN) {
            teamNumber = TN;
        }

        public void setTeamName(String TeamName) {
            teamName = TeamName;
        }

        public PlayerShort(String Name, Integer tNumber, String tName) {
            setName(Name);
            setTeamNumber(tNumber);
            setTeamName(tName);
            setScore(0);
            setWhiteDamage(0);
            setYellowDamage(0);
            myWeapon = new WeaponList();
        }

        public void addWeapon (Weapon weapon){
            myWeapon.add(weapon);
        }
        public WeaponList getWeaponList(){
            return myWeapon;
        }

        public void setScore(Integer score) {
            this.score = score;
        }


        public void setWhiteDamage(Integer whiteDamage) {
            this.whiteDamage = whiteDamage;
        }

        @Override
        public int compareTo(PlayerShort o) {
            int l = Math.min(o.getName().length(), this.getName().length());
            int kk;
            int ll;
            for (int ii = 0; ii < l; ++ii) {
                kk = ORDER.indexOf(o.getName().charAt(ii));
                ll = ORDER.indexOf(this.getName().charAt(ii));
                if (kk == ll)
                    continue;
                else {
                    if (kk < ll)
                        return 1;
                    else
                        return -1;
                }
            }
            return 1;
        }
    }


    public String readMyName() {
        String name = " ";
        if (!St.equals(" ")) {
            File F = new File(St);
            BufferedReader BR;
            try {
                BR = new BufferedReader(new java.io.FileReader(F.getParent() + "\\game.log"));
            } catch (FileNotFoundException e) {
                System.out.println("Ошибка чтения лога с Логином (файл не найден).");
                e.printStackTrace();
                return " ";
            }
            String currentLine;
            while (true) {
                try {
                    currentLine = BR.readLine();
                } catch (IOException e) {
                    System.out.println("Ошибка чтения файла с логином");

                    e.printStackTrace();
                    return " ";
                }
                if (currentLine == null)
                    break;
                String ptn = " negotiation complete: uid";
                if (currentLine.contains(ptn)) {
                    String ptn2 = "nickName ";
                    if (currentLine.contains(ptn2)) {
                        int inx = currentLine.indexOf(ptn2);
                        String forTrim = currentLine.substring(inx + ptn2.length() + 1);
                        name = forTrim.substring(0, forTrim.indexOf("',"));
                    }
                }

            }


        }

        return name;
    }



    public void setTextArea(JTextArea TA) {
        textA = TA;
    }

    public void setTeamNumber(Integer N) {
        teamNumber = N;
    }


    public static String parseVictim(String S) {
        String pt = "Killed. Victim: ";
        Integer index = S.indexOf(pt);
        if (index < 0)
            return "";
        String secondPart = S.substring(index + pt.length());
        Integer index2 = secondPart.indexOf(" ");
        String Victim = S.substring(index + pt.length(), index + pt.length() + index2);
        return Victim;
    }

    public static String parseKiller(String S) {
        String pt2 = "killer: ";
        int index = S.indexOf(pt2);
        if (index < 0)
            return "";
        String secondPart = S.substring(index + pt2.length());
        int index2 = secondPart.indexOf(" ");
        String Killer;
        if (index2 == -1) {
            Killer = S.substring(index + pt2.length());
            //PrintCommand("Ошибка извлеченияимени Killer из " + currentStr);
        } else
            Killer = S.substring(index + pt2.length(), index + pt2.length() + index2);
        return Killer;
    }

    public static String parseDate(String S) {
        String ptr = "--- Date: ";
        int ind = S.indexOf(ptr);
        if (ind < 0)
            return "";
        //System.out.println("Это дата");
        String tmp = S.substring(ind + ptr.length());
        //System.out.println("TMP:"+tmp);
        String outS = tmp.substring(0, tmp.indexOf(' '));
        //System.out.println("Нашли дату:" + outS);

        return outS;
    }

    public static String parseTime(String S) {
        int ind = S.indexOf(' ');
        String tmp = S.substring(0, ind);
        if (tmp.length() != 12) {
            tmp = "";
            System.out.println("Ошибка чтения времени");
        }
        return tmp;
    }

    private void scoreDetector(String s) {
        final String patternScore = " Score:\t\tplayer:";
        final String patternNick = "nick:";
        final String patternGot = "Got:";
        String tmp;
        if (s.contains(patternScore)) {
            tmp = s.substring(s.indexOf(patternNick) + patternNick.length());
            String nickname = tmp.substring(tmp.indexOf(" "), tmp.indexOf(",")).replace(" ", "");
            tmp = s.substring(s.indexOf(patternGot) + patternGot.length());
            String score = tmp.substring(0, tmp.indexOf(",")).replace(" ", "");
            for (PlayerShort player : playersListFull) {
                if (player.getName().equals(nickname)) {
                    player.setScore(player.getScore() + (int) (Math.floor(Integer.parseInt(score))));
                    //System.out.println(player.getName()+ " "+player.getWhiteDamage() );
                    break;
                }
            }
        }
    }

    private void damageDetector(String s) {
        final String patternDamage = "| Damage. Victim: ";
        final String patternAttacker = "attacker: ";
        final String constDamage = "damage: ";
        final String constWeapon = "weapon '";
        String tmp;
        if (s.contains(patternDamage)) {
            int damageInt = 0;
            tmp = s.substring(s.indexOf(patternDamage) + patternDamage.length());
            String victimsName = tmp.substring(0, tmp.indexOf(" "));
            String killerName = tmp.substring(tmp.indexOf(patternAttacker) + patternAttacker.length());
            killerName = killerName.substring(0, killerName.indexOf(" "));
            tmp = s.substring(s.indexOf(constDamage) + constDamage.length());
            double damage = Double.parseDouble(tmp.substring(0, tmp.indexOf(" ")));
            Weapon weapon = null;
            String weaponsName = "";
            if (s.contains(constWeapon)){
                weaponsName = s.substring(s.indexOf(constWeapon)+constWeapon.length());
                weaponsName = weaponsName.substring(0,weaponsName.indexOf("'"));
                if (weaponsName.contains(":"))
                    weaponsName = weaponsName.substring(0,weaponsName.indexOf(":"));
            }


            //System.out.println(killerName);
            if (!victimsName.equals("n/a") && !victimsName.equals(killerName) && !victimsName.contains(":"))
                for (PlayerShort player : playersListFull) {
                    if (player.getName().equals(killerName)) {
                        damageInt=  (int) Math.floor(damage);
                        player.setWhiteDamage(player.getWhiteDamage()+damageInt);
                        if (s.contains("HUD_IMPORTANT"))
                            player.setYellowDamage(player.getYellowDamage()+damageInt);
                        if (!weaponsName.isEmpty()){
                            weapon = new Weapon(weaponsName,damageInt);
                            player.addWeapon(weapon);
                        }
                        break;
                    }
                }
        }
    }

    private synchronized void AdvancedCheck(String currentStr) {
        if (typeOfFile == 0) {
            damageDetector(currentStr);
            scoreDetector(currentStr);

            String pt = "Killed. Victim: ";
            //String pt2 ="killer: ";
            if (currentStr.contains(pt)) {
                //----------------------
                String myName = getMyName();
                for (Player pl : Players) {
                    if (pl.getName().equals(myName)) {
                        teamNumber = pl.getTeamNumber();
                        //PrintCommand("Твоя команда найдена. Это " + teamNumber);
                    }
                }

                //PrintCommand("Размер списка: "+ Players.size());
                //------------------------
                String Victim = parseVictim(currentStr);
                String Killer = parseKiller(currentStr);


                if ((!Victim.equals("n/a")) && (!Killer.equals("n/a"))) {
                    //Определить номер своей команды
                    //Определить номер команды жертвы
                    //Сравнить имена жертвы и убийцы
                    if (!Victim.equals(Killer)) {
                        boolean myteammate = false;
                        for (Player plr : Players) {
                            if (plr.getName().equals(Killer)) {
                                myteammate = teamNumber == plr.getTeamNumber();
                            }
                        }

                        printCommand(currentStr);
                    }


                }


            }


        } else if (typeOfFile == 1) {
            String pAdd = "client: ADD_PLAYER ";
            String pReserved = "RESERVE_PLAYER ";
            if (currentStr.contains(pAdd) || currentStr.contains(pReserved)) {
                String currentPattern;
                if (currentStr.contains(pAdd))
                    currentPattern = pAdd;
                else
                    currentPattern = pReserved;
                //PrintCommand("Попытка добавить игрока");
                //PrintCommand("Строка: "+ currentStr);
                int ix1 = currentStr.indexOf(currentPattern);
                String Str1 = currentStr.substring(ix1 + currentPattern.length());
                String nmb1;
                if (currentStr.charAt(ix1 + currentPattern.length()) == ' ') {
                    nmb1 = Str1.substring(1, 2);
                } else
                    nmb1 = Str1.substring(0, 2);
                Integer NumberOfPl = Integer.parseInt(nmb1); // Номер игрока
                //PrintCommand("Номер игрока: "+NumberOfPl);

                String NameP = currentStr.substring(ix1 + currentPattern.length() + 2, currentStr.indexOf(','));
                NameP = NameP.substring(NameP.lastIndexOf(' ') + 1);
                //PrintCommand("Имя игрока: "+NameP);

                String NumberOfTeam = currentStr.substring(currentStr.indexOf("team ") + 5, currentStr.indexOf("team ") + 1 + 5);
                //PrintCommand("Команда игрока: "+NumberOfTeam);
                Integer nTm = Integer.parseInt(NumberOfTeam);

                String uid = currentStr.substring(currentStr.indexOf("uid ") + 4);
                uid = uid.substring(0, uid.indexOf(' '));

                Player JJ = new Player(NameP, nTm, NumberOfPl, uid);
                //JJ.setNumber(NumberOfPl);
                //PrintCommand("Перед добалением ");
                Players.add(JJ);
                printCommand("Добавлен " + JJ.getNumber() + " " + JJ.getName() + " " + JJ.getTeamNumber() + " " + uid);


            } else {
                String pLeave = "| client: player ";
                if (currentStr.contains(pLeave)) {
                    int ix = currentStr.indexOf(pLeave);
                    String nm;
                    if (currentStr.charAt(ix + pLeave.length() + 1) == ' ')
                        nm = currentStr.substring(ix + pLeave.length(), pLeave.length() + ix + 1);
                    else
                        nm = currentStr.substring(ix + pLeave.length(), pLeave.length() + ix + 2);
                    int nmb = Integer.parseInt(nm);
                    for (Player Plr : Players) {
                        if (nmb == Plr.getNumber()) {
                            printCommand("Удален " + Plr.getNumber() + " " + Plr.getName() + " " + Plr.getTeamNumber());
                            Players.remove(Plr);

                            break;
                        }
                    }
                } else {
                    String pClear = "| ====== starting level";
                    if (currentStr.contains(pClear)) {
                        if (Players != null) {
                            Players.removeAll(Players);
                            printCommand("Очищен список");
                        }
                    }
                }
            }


        } else {
            printCommand("Ошибочный тип файла");
        }

    }




    public void printToFile(String s) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("./party.txt");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write(s); // json is your data
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void printTeamsDataToFiles() {
        ArrayList<PlayerShort> T1 = new ArrayList<>();
        ArrayList<PlayerShort> T2 = new ArrayList<>();
        ArrayList<PlayerShort> a;
        ArrayList<PlayerShort> b;

        for (PlayerShort ps : PList) {
            switch (ps.getTeamNumber()) {
                case 1: {
                    T1.add(ps);
                    break;
                }
                case 2: {
                    T2.add(ps);
                    break;
                }
            }
        }
        if (getTeamNumber().equals(1)) {
            b = T2;
            a = T1;
        } else {
            b = T1;
            a = T2;
        }
        Collections.sort(b);
        PlayerShort myProfile = null;
        for (PlayerShort player : PList) {
            if (player.getName().equals(getMyName())) {
                myProfile = player;
            }
        }
        if (myProfile == null) {
            System.err.println("ERROR: myProfile is null");
        }
        Collections.sort(a);
        a.remove(myProfile);
        ArrayList<PlayerShort> newA = new ArrayList<PlayerShort>();
        newA.add(myProfile);
        if (!myProfile.getTeamName().equals(NO_TEAM)) {
            ArrayList<PlayerShort> myTeamList = new ArrayList<>();
            for (PlayerShort p : a) {
                if (p.getTeamName().equals(myProfile.getTeamName())) {
                    myTeamList.add(p);
                }
            }
            a.removeAll(myTeamList);
            newA.addAll(myTeamList);
        }
        newA.addAll(a);
        saveToFile(getFormattedTeamList(newA,false,numberWeapons), "./teamA.txt");
        saveToFile(getFormattedTeamList(b,true,numberWeapons), "./teamB.txt");
        printCommand(getFormattedTeamList(newA,false,numberWeapons));
        playersListFull.addAll(newA);
        playersListFull.addAll(b);
    }

    private synchronized void saveToFile(String s, String path) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            outputStreamWriter.write(s);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isGroupHomogenious(List<PlayerShort> pList){
        boolean result = false;
        int groupSize = 0;
        for (PlayerShort player : pList){
            if (player.getTeamName().equals(pList.get(0).getTeamName()))
                groupSize++;
        }
        if (groupSize == pList.size())
            result = true;
        return result;
    }
    private String getFormattedTeamList(List<PlayerShort> pList, boolean addWeapon, int maxWeaponCount) {
        boolean groupHomogenuity = isGroupHomogenious(pList);
        StringBuilder playersList = new StringBuilder();
        for (PlayerShort player : pList) {
            String formString;
            formString = String.format("%5d (%5d) %5d |%16s| ", player.getWhiteDamage(),player.getYellowDamage(),player.getScore(),player.getName());
            playersList.append(formString);
            //playersList.append(player.getName());
            //if (!player.getTeamName().equals(NO_TEAM)&&!groupHomogenuity) {
            //    playersList.append(" :: [").append(player.getTeamName()).append("] ");
            //}
            /*
            playersList.append(" ").append(player.getWhiteDamage().toString()).append(" ")
                    .append("(").append(player.getYellowDamage()).append(")")
                    .append(player.getScore()).append(" ");
                    //*/
            ArrayList<Weapon> list = null;
            WeaponList weaponList = player.getWeaponList();
             list = weaponList.getList();
            if (list != null && !list.isEmpty() && addWeapon) {
                ArrayList<Weapon> sortedList = new ArrayList<>(list);
                Collections.sort(sortedList, new Comparator<Weapon>() {
                    @Override
                    public int compare(Weapon o1, Weapon o2) {
                        return o2.getDamage()-o1.getDamage();
                    }
                });
                int counter = 0;
                String weaponName;
                for (Weapon weapon : sortedList) {
                    weaponName= dictionary.findItem(weapon.getName().replace("CarPart_","").toLowerCase());
                    if (weaponName == null || weaponName.isEmpty())
                        weaponName=weapon.getName();
                    playersList.append(weaponName).append(" ").append(weapon.getDamage()) .append(", ");
                    counter++;
                    if (counter>maxWeaponCount-1){
                        break;
                    }
                }
                //playersList.substring(0, playersList.lastIndexOf(","));
            }
            playersList.append("\n");
        }
        return playersList.toString();
    }

    public void PrintTeammatesInfo() {
        if (PList == null) {
            printCommand("ОШИБКА. Попытка печати несозданного списка тиммейтов.");
        } else {
            printTeamsDataToFiles(); // Печать списков команд в файлы
            int partyCounter = 0;
            for (PlayerShort p : PList) {
                if (!p.getTeamName().equals(NO_TEAM))
                    partyCounter++;
            }
            if (partyCounter == 0) {
                printCommand("-----------------------");
                printCommand("Список теммейтов пуст.");
                printToFile("Список теммейтов пуст.");
                printCommand("-----------------------");
            } else {


                String tmp = "";
                String cmd = "Команда: ";
                String players = ". Её игроки: ";


                ArrayList<PlayerShort> T1 = new ArrayList();
                ArrayList<PlayerShort> T2 = new ArrayList();

                for (PlayerShort ps : PList) {
                    switch (ps.getTeamNumber()) {
                        case 1: {
                            T1.add(ps);
                            break;
                        }
                        case 2: {
                            T2.add(ps);
                            break;
                        }
                    }
                }

                ArrayList<PlayerShort> TT;

                for (int ii = 1; ii <= 2; ++ii) {
                    if (ii == 1)
                        TT = T1;
                    else
                        //if (ii==2)
                        TT = T2;

                    while (!TT.isEmpty()) {
                        ArrayList<PlayerShort> tmpL = new ArrayList();
                        String TeamName1 = TT.get(0).getTeamName();
                        if (!TeamName1.equals("00000000"))
                            tmp = tmp + cmd + TeamName1 + players;
                        boolean first = true;
                        for (PlayerShort p : TT) {
                            if (TeamName1.equals(p.getTeamName())) {
                                tmpL.add(p);
                                if (!p.getTeamName().equals("00000000"))
                                    if (first == true) {
                                        tmp += p.getName();
                                        first = false;
                                    } else
                                        tmp += ", " + p.getName();

                            }
                        }
                        if (!TeamName1.equals("00000000"))
                            tmp += ". ";
                        //tmp+="\n";
                        TT.removeAll(tmpL);
                    }


                }


                StringSelection stringSelection = new StringSelection(tmp);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);

                printCommand("-----------------------");
                printCommand(tmp);
                printCommand("-----------------------");


            }
        }
    }


    private void CheckTeammates(String S) {
        String pattern1 = "| \tplayer";
        String CurrentTeamName = "";
        String CurrentName = "";
        Integer CurrentNumber = 0;
        if (S.contains(pattern1)) {
            //CountOfPlayers++;
            //Найти название группы
            String pattern2 = "party ";
            String tmpStr1 = S.substring(S.indexOf(pattern2) + pattern2.length());
            CurrentTeamName = tmpStr1.substring(0, tmpStr1.indexOf(','));
            //Найти имя игрока
            String pattern3 = "nickname: ";
            tmpStr1 = S.substring(S.indexOf(pattern3) + pattern3.length());
            CurrentName = tmpStr1.substring(0, tmpStr1.indexOf(' '));
            //Найти номер группы
            String pattern4 = "team: ";
            tmpStr1 = S.substring(S.indexOf(pattern4) + pattern4.length());
            CurrentNumber = Integer.parseInt(tmpStr1.substring(0, tmpStr1.indexOf(',')));

            if (CurrentName.equals(getMyName())) {
                setTeamNumber(CurrentNumber);
            }
            InfoAboutTeams = 1;
            PList.add(new PlayerShort(CurrentName, CurrentNumber, CurrentTeamName));
            if (PList.size() == 16) {
                PrintTeammatesInfo();
                InfoAboutTeams = 0;
            }
        } else {
            if (InfoAboutTeams == 1) {
                PrintTeammatesInfo();
                InfoAboutTeams = 0;
            }

           // if (S.contains("====== starting level")) {
                if (S.contains(" ===== Gameplay") && (!S.contains("finish"))||(S.contains("| Active battle started."))) {
                //CountOfPlayers = 0;
                if (PList != null){
                    printCommand(getFormattedTeamList(PList,true,3));
                    PList.removeAll(PList);
                    //saveBoolean.set(1);
                }
                else
                    System.out.println("Список не был создан. ОШИБКА");


                if (playersListFull != null)
                    playersListFull.removeAll(playersListFull);
            }

        }

    }


    public void SetPath(String Str) {
        St = Str;
    }



    @Override
    //*
    public void run() {
        BufferedReader inputLog = null;
        String St1 = St;
        boolean isItFirstTime = false;

        try {
            inputLog = new BufferedReader(new java.io.FileReader(St1));
            String currentStr = null;
            do {
                if (!Thread.interrupted())
                {
                    try {
                        currentStr = inputLog.readLine();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        System.out.println("Ошибка чтения файла");
                        //inputLog.close();
                    }
                    if (currentStr != null) {
                        //System.out.println(currentStr);
                        boolean isMatchFound = false;

                        if (isItFirstTime == false) {
                            CheckTeammates(currentStr);
                            isMatchFound = false;
                            if (!isMatchFound) {
                                AdvancedCheck(currentStr);
                            }
                        }
                        continue;
                    } else {
                        isItFirstTime = false;
                    }

                } else
                    return;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
            while (true);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            System.out.println("Ошибка открытия файла");
            //e1.toString();

        } finally {
            try {
                if (inputLog != null) {
                    inputLog.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("Ошибка закрытия файла");
                e.printStackTrace();
            }
        }
    }

}
