package cc.gu.util.value;

public interface ValuableChangedListener <V>{
	public void onValueChanged(Valuable<? extends V> valuable);
}
