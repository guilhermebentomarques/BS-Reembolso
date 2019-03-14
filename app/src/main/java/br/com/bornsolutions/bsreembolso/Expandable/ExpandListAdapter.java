package br.com.bornsolutions.bsreembolso.Expandable;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.bornsolutions.bsreembolso.R;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Group> groups;

	public ExpandListAdapter(Context context, ArrayList<Group> groups) {
		this.context = context;
		this.groups = groups;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<Child> chList = groups.get(groupPosition)
				.getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		Child child = (Child) getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expand_detalhe, null);
		}

		TextView tvData = (TextView) convertView.findViewById(R.id.ldtxtData);
		TextView tvValor = (TextView) convertView.findViewById(R.id.ldtxtValor);

		tvData.setText(child.getData().toString());
		tvValor.setText(child.getValor().toString());

		// tv.setText(child.getName().toString()+"::"+child.getTag());
		// tv.setTag(child.getTag());
		// TODO Auto-generated method stub
		return convertView;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<Child> chList = groups.get(groupPosition).getItems();

		return chList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		Group group = (Group) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.expand_mestre, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.txtTipoDespesa);
		tv.setText(group.getName());

		TextView tvValor = (TextView) convertView.findViewById(R.id.txtValor);
		tvValor.setText(group.getValor());
		// TODO Auto-generated method stub
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
