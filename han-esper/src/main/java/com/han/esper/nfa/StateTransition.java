package com.han.esper.nfa;

public class StateTransition
{

	private State srcState;

	private State targetState;

	private String inputStr;

	public State getSrcState()
	{
		return srcState;
	}

	public void setSrcState(State srcState)
	{
		this.srcState = srcState;
	}

	public State getTargetState()
	{
		return targetState;
	}

	public void setTargetState(State targetState)
	{
		this.targetState = targetState;
	}

	public String getInputStr()
	{
		return inputStr;
	}

	public void setInputStr(String inputStr)
	{
		this.inputStr = inputStr;
	}

}
