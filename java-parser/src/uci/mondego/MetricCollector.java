package uci.mondego;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

public class MetricCollector {
    public File _file; // file object
    public String _methodName; // method name
    public List<String> actionTokens;
    public int CAST; // number of class casts (Integer)x
    public int COMP; // McCabes cyclomatic complexity
    public int CREF; // Number of classes referenced
    public int END_LINE; // end line of this method
    public int EXCR; // Number of exceptions referenced
    public int EXCT; // Number of exceptions thrown
    public List<String> fieldAccessActionTokens = new ArrayList<String>();
    public int HDIF; // Halstead difficulty to implement a method
    public double HEFF; // Halstead effort to implement a method
    private int HLTH; // halstead length. not part of features
    public int HVOC; // Halstead vocabulary of a method
    private double HVOL; // halstead volumn. not part of features
    public int LMET; // local methods called by method
    public int LOOP; // number of loops
    public int MDN; // Maximum depth of nesting in a method
    public List<String> methodCallActionTokens = new ArrayList<String>();
    public int NAND; // number of operands
    // http://www.verifysoft.com/en_halstead_metrics.html
    /*
     * Tokens of the following categories are all counted as operants by CMT++:
     * 1) IDENTIFIER all identifiers that are not reserved words.; 2) TYPENAME.;
     * TYPESPEC (type specifiers) Reserved words that specify type: bool, char,
     * double, float, int, long, short, signed, unsigned, void. This class also
     * includes some compiler specific nonstandard keywords.; CONSTANT
     * Character, numeric or string constants.
     */
    public int NEXP; // number of expressions
    public int NOA; // number of arguments
    public int NOPR; // number of operators
    public int NOS; // number of statements
    public int NTOKENS; // number of tokens
    public int numIf; // number of if statements
    public List<String> operands = new ArrayList<String>();
    public List<String> operators = new ArrayList<String>();
    public List<String> removeFromOperands = new ArrayList<String>();
    public int START_LINE; // start line of this method
    public List<String> tokens = new ArrayList<String>();
    public int UNAND; // unique number of operands
    public int UNPOR; // unique number of operators
    public int VDEC; // Number of variables declared
    public int VREF; // number of variables referenced
    public int XMET; // number of external methods called by the method

    public void addFieldAccessActionTokens(String fieldAccessString) {
        String[] tokens = fieldAccessString.split("\\.");
        // ignore the 1st one
        this.NOPR = this.NOPR + tokens.length-1; // accounting for "." operators
        if(tokens.length>1){
            for (int i=1;i<tokens.length;i++){
                this.fieldAccessActionTokens.add(tokens[i]);
            }
        }
    }

    public void addMethodCallActionToken(String token) {
        this.methodCallActionTokens.add(token+"()");
        this.removeFromOperands.add(token);
        this.operators.add(token);
        this.NOPR++; // accounting for functionCall Operator
    }

    public void addOperand(String token) {
        if (!KeywordsJava.reserved.contains(token)) {
            this.operands.add(token);
            this.NAND++;
        }
    }
    
    public void addToken(String token){
        this.tokens.add(token);
        if(KeywordsJava.operators.contains(token)){
            this.operators.add(token);
            this.NOPR++;
        }else{
            this.operands.add(token);
        }
        this.NTOKENS++;
    }
    
    public void computeHalsteadMetrics(){
        //http://www.virtualmachinery.com/sidebar2.htm
        this.operands=(List<String>) CollectionUtils.subtract(this.operands , this.removeFromOperands);
        this.NAND = this.operands.size();
        Set<String> UNAND = new HashSet<String>(this.operands);
        this.UNAND = UNAND.size();
        Set<String> UNPOR = new HashSet<String>(this.operators);
        this.UNPOR = UNPOR.size();
        this.setHLTH();
        this.setHVOC();
        this.setHDIF();
        this.setHVOL();
        this.setHEFF();
    }
    private void setHDIF(){
        this.HDIF = (this.UNPOR/2) * this.NAND/this.UNAND;
    }
    private void setHEFF(){
        this.HEFF = this.HDIF*this.HVOL;
    }
    private void setHLTH(){
        this.HLTH = this.NAND+this.NOPR;
    }
    private void setHVOC(){
        this.HVOC = this.UNAND+this.UNPOR;
    }
    private void setHVOL(){
        this.HVOL = this.HLTH * (Math.log(this.HVOC)/Math.log(2)); // logb(n) = loge(n) / loge(b)
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MetricCollector [_file=" + _file + ", _methodName="
                + _methodName + ", actionTokens=" + actionTokens + ", CAST="
                + CAST + ", COMP=" + COMP + ", CREF=" + CREF + ", END_LINE="
                + END_LINE + ", EXCR=" + EXCR + ", EXCT=" + EXCT
                + ", fieldAccessActionTokens=" + fieldAccessActionTokens
                + ", HDIF=" + HDIF + ", HEFF=" + HEFF + ", HLTH=" + HLTH
                + ", HVOC=" + HVOC + ", HVOL=" + HVOL + ", LMET=" + LMET
                + ", LOOP=" + LOOP + ", MDN=" + MDN
                + ", methodCallActionTokens=" + methodCallActionTokens
                + ", NAND=" + NAND + ", NEXP=" + NEXP + ", NOA=" + NOA
                + ", NOPR=" + NOPR + ", NOS=" + NOS + ", NTOKENS=" + NTOKENS
                + ", numIf=" + numIf + ", operands=" + operands + ", operators="
                + operators + ", removeFromOperands=" + removeFromOperands
                + ", START_LINE=" + START_LINE + ", tokens=" + tokens
                + ", UNAND=" + UNAND + ", UNPOR=" + UNPOR + ", VDEC=" + VDEC
                + ", VREF=" + VREF + ", XMET=" + XMET + "]";
    }

}
