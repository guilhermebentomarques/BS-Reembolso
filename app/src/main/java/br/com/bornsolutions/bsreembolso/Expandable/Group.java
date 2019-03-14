package br.com.bornsolutions.bsreembolso.Expandable;

import java.util.ArrayList;

public class Group {
 
	private String Name;

	public String getValor() {
		return Valor;
	}

	public void setValor(String valor) {
		Valor = valor;
	}

	private String Valor;
	private ArrayList<Child> Items;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}

	public ArrayList<Child> getItems() {
		return Items;
	}
	public void setItems(ArrayList<Child> Items) {
		this.Items = Items;
	}
	
	
}
