package wait.wowrss;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelObject implements Parcelable {

	public String parselStringArray;
	
	public ParcelObject(String array)
	{
		this.parselStringArray = array;
	}
	
	private ParcelObject(Parcel p)
	{
		this.parselStringArray = p.readString();
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p, int flags) {
		p.writeString(this.parselStringArray);
	}
	
	public static final Parcelable.Creator<ParcelObject> CREATOR = new Parcelable.Creator<ParcelObject>() 
		{  
	    	public ParcelObject createFromParcel(Parcel in) {
	    	return new ParcelObject(in);
	    }

	    public ParcelObject[] newArray(int size) {
	      return new ParcelObject[size];
	    }
	  };

}
