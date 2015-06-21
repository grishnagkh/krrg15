import java.util.ArrayList;
import java.util.List;

public class EvalPCFabian extends PieceCounter {

    private SeqFabian seq;

    public EvalPCFabian(GameLogic gl) {
        super(gl);
        seq = new SeqFabian();
    }

    @Override
    public int eval(State s) {
        int sumOwn = 0, sumOpponent = 0;

        //Rows
        for (int[] rows : s.gameBoard) {
            seq.push(rows);
            sumOwn += seq.eval(pid);
            if (sumOwn == Integer.MAX_VALUE) {
                return sumOwn;
            }

            sumOpponent += seq.eval(oid);
            seq.flush();
        }

        //Columns
        for (int col = 0; col < s.gameBoard[0].length; col++) {
            for (int[] row : s.gameBoard) {
                seq.push(row[col]);
            }
            sumOwn += seq.eval(pid);
            if (sumOwn == Integer.MAX_VALUE) {
                return sumOwn;
            }
            sumOpponent += seq.eval(oid);
            seq.flush();
        }

        //Diagonals right/up
        for (int k = 3; k < s.gameBoard.length + s.gameBoard[0].length - 4; k++) {
            for (int j = 0; j <= k; j++) {
                int i = k - j;
                if (i < s.gameBoard.length && j < s.gameBoard[i].length) {
                    seq.push(s.gameBoard[i][j]);
                }
            }

            sumOwn += seq.eval(pid);
            if (sumOwn == Integer.MAX_VALUE) {
                return sumOwn;
            }
            sumOpponent += seq.eval(oid);
            seq.flush();
        }

        //Diagonals right/down
        for (int k = 3; k < s.gameBoard.length + s.gameBoard[0].length - 4; k++) {
            for (int j = 0; j <= k; j++) {
                int i = k - j;
                if (i < s.gameBoard.length && j < s.gameBoard[i].length) {
                    seq.push(s.gameBoard[i][s.gameBoard[i].length - j - 1]);
                }
            }

            sumOwn += seq.eval(pid);
            if (sumOwn == Integer.MAX_VALUE) {
                return sumOwn;
            }
            sumOpponent += seq.eval(oid);
            seq.flush();
        }

        return sumOwn - sumOpponent;
    }

    private class SeqFabian {

        List<Integer> sequence = new ArrayList<Integer>(7);

        public void push(int field) {
            sequence.add(field);
        }

        public void push(int... fields) {
            for (int i : fields) {
                sequence.add(i);
            }
        }

        public void flush() {
            sequence.clear();
        }

        public int eval(int pid) {
            int sum = 0;
            boolean open = false;
            int length = 0;

            for (Integer field : sequence) {
                if (field == 0) {
                    sum = weigth(length, sum);
                    open = true;
                    length = 0;
                } else if (field == pid) {
                    length++;
                } else {
                    length = 0;
                    open = false;
                }
            }
            if (open) {
                sum = weigth(length, sum);
            }

            return sum;
        }


        public int weigth(int length, int sum) {
            switch (length) {
                case 4:
                    //Should not be accessed
                    sum = Integer.MAX_VALUE;
                    break;
                case 3:
                    sum += 3;
                    break;
                case 2:
                    sum += 1;
                    break;
                default:
                    break;
            }
            return sum;
        }
    }


}