package com.han.esper.nfa;

import java.util.ArrayList;
import java.util.List;

public class State
{

	private String name;

	private List<StateTransition> stateTransitions;

	public State(String name)
	{
		super();
		this.name = name;

		this.stateTransitions = new ArrayList<StateTransition>();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<StateTransition> getStateTransitions()
	{
		return stateTransitions;
	}

	public void setStateTransitions(List<StateTransition> stateTransitions)
	{
		this.stateTransitions = stateTransitions;
	}

	public void addStateTransition(StateTransition stateTransition)
	{
		this.stateTransitions.add(stateTransition);
	}

}
