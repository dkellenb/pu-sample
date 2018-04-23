package com.mwiesner.pto.domain;

import java.util.List;

public interface PTOQueryInPort {

	List<PTO> getAllPTOs();

	PTO getPTO(String id);

}
