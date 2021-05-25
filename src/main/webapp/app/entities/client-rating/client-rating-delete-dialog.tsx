import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './client-rating.reducer';

export interface IClientRatingDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ClientRatingDeleteDialog = (props: IClientRatingDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/client-rating' + props.location.search);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.clientRatingEntity.id);
  };

  const { clientRatingEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="clientRatingDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="jusnexoApp.clientRating.delete.question">Are you sure you want to delete this ClientRating?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-clientRating" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ clientRating }: IRootState) => ({
  clientRatingEntity: clientRating.entity,
  updateSuccess: clientRating.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClientRatingDeleteDialog);
