import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './chat.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IChatDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ChatDetail = (props: IChatDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { chatEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chatDetailsHeading">Chat</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{chatEntity.id}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{chatEntity.createdAt ? <TextFormat value={chatEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>Client Receiver</dt>
          <dd>
            {chatEntity.clientReceivers
              ? chatEntity.clientReceivers.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {chatEntity.clientReceivers && i === chatEntity.clientReceivers.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Client Sender</dt>
          <dd>
            {chatEntity.clientSenders
              ? chatEntity.clientSenders.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {chatEntity.clientSenders && i === chatEntity.clientSenders.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/chat" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chat/${chatEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ chat }: IRootState) => ({
  chatEntity: chat.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ChatDetail);
