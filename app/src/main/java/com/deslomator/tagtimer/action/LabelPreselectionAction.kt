package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label

sealed interface LabelPreselectionAction {
    /*
    TAG
     */
    class SelectTagCheckedChange(val tag: Label.Tag, val checked: Boolean): LabelPreselectionAction
    class EditTagClicked(val tag: Label.Tag): LabelPreselectionAction
    class AcceptTagEditionClicked(val name: String, val color: String): LabelPreselectionAction
    data object AddNewTagClicked: LabelPreselectionAction
    data object DismissTagDialog: LabelPreselectionAction
    class DeleteTagClicked(val tag: Label.Tag): LabelPreselectionAction
    /*
    PERSON
     */
    class SelectPersonCheckedChange(val person: Label.Person, val checked: Boolean): LabelPreselectionAction
    class EditPersonClicked(val person: Label.Person): LabelPreselectionAction
    class AcceptPersonEditionClicked(val name: String, val color: String): LabelPreselectionAction
    data object AddNewPersonClicked: LabelPreselectionAction
    data object DismissPersonDialog: LabelPreselectionAction
    class DeletePersonClicked(val person: Label.Person): LabelPreselectionAction
    /*
    PLACE
     */
    class SelectPlaceCheckedChange(val place: Label.Place, val checked: Boolean): LabelPreselectionAction
    class EditPlaceClicked(val place: Label.Place): LabelPreselectionAction
    class AcceptPlaceEditionClicked(val name: String, val color: String): LabelPreselectionAction
    data object AddNewPlaceClicked: LabelPreselectionAction
    data object DismissPlaceDialog: LabelPreselectionAction
    class DeletePlaceClicked(val place: Label.Place): LabelPreselectionAction
}