package open_payments.api;

class Interaction {
        String[] start;
        Finish finish;
        Interaction(String[] start){
            this.start = start;
        }
        Interaction(String[] start, Finish finish){
            this.start = start;
            this.finish = finish;
        } 
}
