package orzu.org;


import android.util.Log;

public class ArrayID {

    private Long[] arrayID;

    public ArrayID() {
    }

    public ArrayID(Long[] arrayID) {
        this.arrayID = arrayID;
    }

    public Long[] getArrayID() {
        return arrayID;
    }

    public void setArrayID(Long [] arrayID) {
        Log.e("TOCLASS", String.valueOf(arrayID[0]));
        this.arrayID = arrayID;
    }

}
