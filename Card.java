
public class Card {
    int rank;
    String suit;
    boolean isFaceUp;

    public Card(int rank, String suit){
        this.rank = rank;
        this.suit = suit;
        isFaceUp = false;
    }
    public int getRank(){
        return rank;
    }
    public String getSuit(){
        return suit;
    }
    public boolean isRed(){
        return (suit == "d" || suit == "h");
    }
    public boolean isFaceUp(){
        return isFaceUp;
    }
    public void turnUp(){
        isFaceUp = true;
    }
    public void turnDown(){
        isFaceUp = false;
    }

    public String rankString(){
        String ret = "";
        if(rank == 1){
            ret += "a" ;
            return ret;
        }        
        if(rank == 11){
            ret += "j" ;
            return ret;
        }        
        if(rank == 12){
            ret += "q" ;
            return ret;
        }        
        if(rank == 13){
            ret += "k" ;
            return ret;
        }  
        if(rank == 10){
            ret += "t";
            return ret;
        }
        else return String.valueOf(rank);  
    }
    public String getFileName(){
        if(!isFaceUp) return "cards\\back.gif";
        return "cards\\" + rankString() + suit + ".gif";
    } 

}
