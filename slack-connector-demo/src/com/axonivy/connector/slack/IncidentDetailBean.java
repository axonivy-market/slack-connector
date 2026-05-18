package com.axonivy.connector.slack;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IUser;

@ManagedBean
@ViewScoped
public class IncidentDetailBean {
	public List<IUser> getUsers() {
		return Ivy.security().users().paged().stream().toList();
	}
}
