package code;

public class State {

    Bottle[] bottles;

    public State(Bottle[] bottles){
        this.bottles = bottles;
    }

    @Override
    public String toString() {
        String s="";
        for(Bottle b:bottles) {
            s=s+b.toString()+";";
        }
        return s;
    }

    //	Heuristic 1 (GR1): Number of Non-Homogeneous Bottles
    //	This heuristic counts the number of bottles that are not yet homogeneous (i.e., bottles where all the liquids aren't the same color or aren't empty).
    //	Admissibility: This is admissible because, at a minimum, each non-homogeneous bottle will require at least one move to sort. It won't overestimate the number of moves needed.
    //	
	public int getHeuristic1(){
		int nonHomogeneousBottles = 0;

		for (Bottle bottle : bottles) {
			if (!bottle.isSameColor()) {
				nonHomogeneousBottles++;
			}
		}

		return nonHomogeneousBottles;
	}

    //	Heuristic 2 (GR2): Number of Individual Colors Out of Place
//	This heuristic counts the total number of misplaced liquids across all bottles. Each color in a bottle that doesn't match the rest of the bottle's contents or isn't in its final, sorted position is considered "out of place."
//
//	Admissibility: Since each misplaced liquid will need to be moved at least once, this heuristic is also admissible. It provides an underestimate of the true number of moves required.
//	
	public int getHeuristic2() {
		int misplacedColors = 0;

		for (Bottle bottle : bottles) {
			misplacedColors += bottle.getMisplacedCount();
		}

		return misplacedColors;
	}
    
}
