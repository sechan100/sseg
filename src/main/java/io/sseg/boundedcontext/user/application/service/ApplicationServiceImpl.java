package io.sseg.boundedcontext.user.application.service;


import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.user.application.entity.Application;
import io.sseg.boundedcontext.user.application.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    
    private final ApplicationRepository applicationRepository;
    private final Rq rq;
    
    
    @Override
    public List<Application> showMyApplications() {
        return null;
    }
    
    
}
