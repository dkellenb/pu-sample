package com.mwiesner.pto.domain;


public interface PTOCommandInPort {

	PTO createPTORequest(PTO ptoRequest);

	void cancelPTORequest(PTO pto);

}
