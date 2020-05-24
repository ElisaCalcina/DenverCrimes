package it.polito.tdp.crimes.model;

//creo la classe per farmi dare le adiacenze dal dao e ottimizzare i tempi
public class Adiacenza {
	private String obj1;
	private String obj2;
	private Double peso;
	
	public Adiacenza(String obj1, String obj2, Double peso) {
		this.obj1 = obj1;
		this.obj2 = obj2;
		this.peso = peso;
	}

	public String getObj1() {
		return obj1;
	}

	public void setObj1(String obj1) {
		this.obj1 = obj1;
	}

	public String getObj2() {
		return obj2;
	}

	public void setObj2(String obj2) {
		this.obj2 = obj2;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	

}
