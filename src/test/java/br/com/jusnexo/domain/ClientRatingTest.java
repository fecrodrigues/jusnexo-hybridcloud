package br.com.jusnexo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.jusnexo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientRatingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientRating.class);
        ClientRating clientRating1 = new ClientRating();
        clientRating1.setId(1L);
        ClientRating clientRating2 = new ClientRating();
        clientRating2.setId(clientRating1.getId());
        assertThat(clientRating1).isEqualTo(clientRating2);
        clientRating2.setId(2L);
        assertThat(clientRating1).isNotEqualTo(clientRating2);
        clientRating1.setId(null);
        assertThat(clientRating1).isNotEqualTo(clientRating2);
    }
}
