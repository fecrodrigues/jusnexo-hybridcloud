import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './area-of-expertise.reducer';

export interface IAreaOfExpertiseDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AreaOfExpertiseDeleteDialog = (props: IAreaOfExpertiseDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/area-of-expertise' + props.location.search);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.areaOfExpertiseEntity.id);
  };

  const { areaOfExpertiseEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="areaOfExpertiseDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="jusnexoApp.areaOfExpertise.delete.question">Are you sure you want to delete this AreaOfExpertise?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-areaOfExpertise" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ areaOfExpertise }: IRootState) => ({
  areaOfExpertiseEntity: areaOfExpertise.entity,
  updateSuccess: areaOfExpertise.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AreaOfExpertiseDeleteDialog);
