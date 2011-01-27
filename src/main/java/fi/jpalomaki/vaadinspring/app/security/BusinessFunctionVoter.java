package fi.jpalomaki.vaadinspring.app.security;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;

/**
 * An {@link AccessDecisionVoter} implementation that votes YES if a user has a
 * matching "BF_"-prefixed granted authority, votes NO if a user does not have
 * a matching "BF_"-prefixed granted authority, and ABSTAINS from voting if
 * the attribute being checked does not begin with "BF_".
 *
 * @author jpalomaki
 */
public final class BusinessFunctionVoter implements AccessDecisionVoter {
    
    private static final String BUSINESS_FUNCTION_PREFIX = "BF_";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute.getAttribute() != null && attribute.getAttribute().startsWith(BUSINESS_FUNCTION_PREFIX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        int result = ACCESS_ABSTAIN;
        Collection<GrantedAuthority> authorities = authentication.getAuthorities();
        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;
                for (GrantedAuthority authority : authorities) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }
        return result;
    }
}
