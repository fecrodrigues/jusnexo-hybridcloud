import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './client-rating.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IClientRatingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ClientRatingDetail = (props: IClientRatingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { clientRatingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clientRatingDetailsHeading">ClientRating</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{clientRatingEntity.id}</dd>
          <dt>
            <span id="score">Score</span>
          </dt>
          <dd>{clientRatingEntity.score}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{clientRatingEntity.description}</dd>
          <dt>Client</dt>
          <dd>{clientRatingEntity.client ? clientRatingEntity.client.id : ''}</dd>
          <dt>Client Evaluator</dt>
          <dd>{clientRatingEntity.clientEvaluator ? clientRatingEntity.clientEvaluator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/client-rating" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/client-rating/${clientRatingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ clientRating }: IRootState) => ({
  clientRatingEntity: clientRating.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClientRatingDetail);
