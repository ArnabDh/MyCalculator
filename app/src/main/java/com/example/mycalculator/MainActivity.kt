package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var canAddOperation=false
    private var canAddDecimal=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }



    fun allClearAction(view: View) {
        working.text=""
        result.text=""
    }
    fun backspaceAction(view: View) {
        val length=working.length()
        if(length>0)
            working.text=working.text.subSequence(0,length-1)
    }
    fun equalsAction(view: View) {
        result.text=calculate()
        history.text=working.text
        working.text=result.text
    }

    private fun calculate(): String {
        val digit = digitFunction()
        if (digit.isEmpty())
            return ""
        val divison=Divison(digit)
        if (divison.isEmpty())
            return ""
        val result=AddSub(divison)
        return result.toString()
    }

    private fun AddSub(digit: MutableList<Any>): Any {
        var result=digit[0] as Float
        for(i in digit.indices)
        {
            if(digit[i] is Char && i!=digit.lastIndex){
                val operator=digit[i]
                val next=digit[i+1] as Float
                if(operator=='+')
                    result+=next
                else
                    result-=next
            }
        }
        return result
    }



    private fun Divison(digit: MutableList<Any>): MutableList<Any> {
        var list=digit
        while(list.contains('X')||list.contains('/'))
        {
            list=divison(digit)
        }
        return list
    }

    private fun divison(digit: MutableList<Any>): MutableList<Any> {
        val new= mutableListOf<Any>()
        var restartIndex=digit.size
        for(i in digit.indices)
        {
            if(digit[i] is Char && i!=digit.lastIndex && i<restartIndex){
                val operator=digit[i]
                val prev=digit[i-1] as Float
                val next=digit[i+1] as Float
                when(operator)
                {
                    'X'-> {
                        new.add(prev * next)
                        restartIndex = i + 1
                    }
                    '/'-> {
                        new.add(prev / next)
                        restartIndex = i + 1
                    }
                    else ->{
                        new.add(prev)
                        new.add(operator)
                    }
                }
            }
            if(i>restartIndex)
                new.add(digit[i])
        }
        return new

    }

    fun operationAction(view: View) {
        if(view is Button && canAddOperation)
        {
            working.append(view.text)
            canAddOperation=false
            canAddDecimal=true
        }
    }
    fun numberAction(view: View) {
        if(view is Button)
        {
            if(view.text==".")
            {
                if(canAddDecimal)
                    working.append(view.text)
                canAddDecimal=false
            }
            else
                working.append(view.text)
            canAddOperation=true
        }

    }

    private fun digitFunction(): MutableList<Any>
    {
        val list= mutableListOf<Any>()
        var current=""
        for( ch in working.text)
        {
            if(ch.isDigit()||ch=='.')
                current+=ch
            else if(ch=='%')
            {
                list.add(current.toFloat()/100)
            }
            else
            {
                list.add(current.toFloat())
                current=""
                list.add(ch)
            }
        }
        if(current!="")
            list.add(current.toFloat())
        return list
    }
}