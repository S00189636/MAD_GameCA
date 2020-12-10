package edu.mohamedshiha.gameca;

public class Score implements Comparable  {
    String Name;
    String ID;
    String Score;
    String Level;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setScore(String score) {
        Score = score;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getScore() {
        return Score;
    }

    public String getLevel() {
        return Level;
    }



    public Score(String id,String name, String level, String score){
        Name = name;
        Score = score;
        Level = level;
        ID = id;
    }

    public Score(String score,String level)
    {
        this("","PlayerName",level,score);
    }

    public Score(String score)
    {
        this(score,"0");
    }

    @Override
    public int compareTo(Object o) {
        //if(o == null) return 0;
        int score =Integer.parseInt(getScore());
        int otherScore = Integer.parseInt(((Score)o).getScore());
        return otherScore -score;
    }
}
