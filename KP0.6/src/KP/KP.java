package KP;

import java.util.Random;

public class KP {

    private String[] card = new String[52];
    private String[] card2 = new String[52];

    public void produceCard() {
        int cardIndex = 0;
        String[] fourColours = {"fang", "mei", "hong", "hei"};
        for (int k = 0; k < 4; k++) {
            for (int i = 1; i <= 13; i++) {
            	if(i<10) {
            		card[cardIndex] = fourColours[k] +"0"+ i + "_调整大小.jpg";
            	}else {
            		card[cardIndex] = fourColours[k] + i + "_调整大小.jpg";
            	}
                cardIndex++;
            }
        }
    }

    public void upsetCard() {   
        int cardLength = card.length;
        int i = 0;
        Random r = new Random();
        while (cardLength > 0) {
            int randomNumber = r.nextInt(cardLength);
            card2[i] = card[randomNumber];
            card[randomNumber] = card[cardLength - 1];
            cardLength--;
            i++;
        }
    }

    public void outputCard() {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
                                                                                                                                                                                  for (String s : card) {
            System.out.println(s);
        }

    }
    
    public String[] fiftyTwoCards() {
    	String[] card3 = new String[52];
    	produceCard();
    	upsetCard();
    	for(int i =0;i<52;i++) {
    		card3[i] = card2[i];
    	}
    
    	return card3;
    	
    }

}
