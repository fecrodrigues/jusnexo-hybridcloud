package br.com.jusnexo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.jusnexo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AreaOfExpertiseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AreaOfExpertise.class);
        AreaOfExpertise areaOfExpertise1 = new AreaOfExpertise();
        areaOfExpertise1.setId(1L);
        AreaOfExpertise areaOfExpertise2 = new AreaOfExpertise();
        areaOfExpertise2.setId(areaOfExpertise1.getId());
        assertThat(areaOfExpertise1).isEqualTo(areaOfExpertise2);
        areaOfExpertise2.setId(2L);
        assertThat(areaOfExpertise1).isNotEqualTo(areaOfExpertise2);
        areaOfExpertise1.setId(null);
        assertThat(areaOfExpertise1).isNotEqualTo(areaOfExpertise2);
    }
}
