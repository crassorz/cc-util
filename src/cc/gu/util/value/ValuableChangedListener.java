package cc.gu.util.value;

public interface ValuableChangedListener <T>{
	public void onValueChanged(Valuable<? extends T> valuable);
}
