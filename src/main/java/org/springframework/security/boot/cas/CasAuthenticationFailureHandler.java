package org.springframework.security.boot.cas;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.boot.SecurityCasAuthcProperties;
import org.springframework.security.boot.biz.ListenedAuthenticationFailureHandler;
import org.springframework.security.boot.biz.authentication.AuthenticationListener;
import org.springframework.security.core.AuthenticationException;

/**
 * Cas认证请求失败后的处理实现
 */
public class CasAuthenticationFailureHandler extends ListenedAuthenticationFailureHandler {
	
	private Logger logger = LoggerFactory.getLogger(CasAuthenticationFailureHandler.class);
	private SecurityCasAuthcProperties authcProperties;
	
	public CasAuthenticationFailureHandler(SecurityCasAuthcProperties authcProperties) {
		super(authcProperties.getLoginUrl());
		this.authcProperties = authcProperties;
	}
	
	public CasAuthenticationFailureHandler(List<AuthenticationListener> authenticationListeners, SecurityCasAuthcProperties authcProperties) {
		super(authenticationListeners, authcProperties.getLoginUrl());
		this.authcProperties = authcProperties;
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {

		String redirectUrl = createRedirectUrl();
		
		logger.debug(redirectUrl);
		logger.error("Failure");
		response.sendRedirect(redirectUrl);
		
		//super.onAuthenticationFailure(request, response, e);
		
	}
	

	/**
	 * Constructs the Url for Redirection to the CAS server. Default implementation relies
	 * on the CAS client to do the bulk of the work.
	 *
	 * @param serviceUrl the service url that should be included.
	 * @return the redirect url. CANNOT be NULL.
	 */
	protected String createRedirectUrl() {
		return CommonUtils.constructRedirectUrl(authcProperties.getLoginUrl(),
				this.authcProperties.getServiceParameterName(), authcProperties.getServiceUrl(),
				this.authcProperties.isRenew(), false);
	}


}
