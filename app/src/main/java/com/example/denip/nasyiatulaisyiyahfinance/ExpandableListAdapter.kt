package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import java.util.HashMap

/**
 * Created by denip on 9/15/2017.
 */

class ExpandableListAdapter(private val _context: Context,
                            private val _listDataHeader: List<String>,
                            private val _listDataChild: HashMap<String, List<String>>)
    : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosititon: Int): Any =
        this._listDataChild[this._listDataHeader[groupPosition]]!![childPosititon]

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getChildView(groupPosition: Int, childPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var _convertView = convertView

        val childText = getChild(groupPosition, childPosition) as String

        if (_convertView == null) {
            val infalInflater = this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            _convertView = infalInflater.inflate(R.layout.list_item, null)
        }

        val txtListChild = _convertView!!
            .findViewById(R.id.lblListItem) as TextView

        txtListChild.text = childText
        return _convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int =
        this._listDataChild[this._listDataHeader[groupPosition]]!!.size

    override fun getGroup(groupPosition: Int): Any = this._listDataHeader[groupPosition]

    override fun getGroupCount(): Int = this._listDataHeader.size

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        var _convertView = convertView
        if (_convertView == null) {
            val infalInflater = this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            _convertView = infalInflater.inflate(R.layout.list_group, null)
        }
        return _convertView!!
    }

    override fun hasStableIds(): Boolean = false

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}