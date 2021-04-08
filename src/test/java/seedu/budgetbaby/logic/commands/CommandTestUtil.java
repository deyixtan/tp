package seedu.budgetbaby.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.budgetbaby.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.budgetbaby.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.budgetbaby.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.budgetbaby.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.budgetbaby.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.budgetbaby.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import seedu.budgetbaby.ablogic.commands.EditCommand;
import seedu.budgetbaby.commons.core.index.Index;
import seedu.budgetbaby.logic.commands.exceptions.CommandException;
import seedu.budgetbaby.model.BudgetBabyModel;
import seedu.budgetbaby.model.BudgetTracker;
import seedu.budgetbaby.model.record.Description;
import seedu.budgetbaby.model.record.FinancialRecord;
import seedu.budgetbaby.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_CATEGORY + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_CATEGORY + VALID_TAG_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_CATEGORY + "hubby*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(BudgetBabyCommand command, BudgetBabyModel actualModel,
                                            CommandResult expectedCommandResult,
                                            BudgetBabyModel expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(BudgetBabyCommand,
     * BudgetBabyModel, CommandResult, BudgetBabyModel)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(BudgetBabyCommand command, BudgetBabyModel actualModel,
                                            String expectedMessage, BudgetBabyModel expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(BudgetBabyCommand command, BudgetBabyModel actualModel,
                                            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        BudgetTracker expectedBudgetTracker = new BudgetTracker(actualModel.getBudgetTracker());
        List<FinancialRecord> expectedFilteredList = new ArrayList<>(actualModel.getFilteredFinancialRecordList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedBudgetTracker, actualModel.getBudgetTracker());
        assertEquals(expectedFilteredList, actualModel.getFilteredFinancialRecordList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the financial record at the given {@code targetIndex} in the
     * {@code model}'s budget list.
     */
    public static void showFrAtIndex(BudgetBabyModel model, List<Index> targetIndex) {
        assertTrue(targetIndex.size() <= model.getFilteredFinancialRecordList().size());
        ArrayList<Description> frList = new ArrayList<>();

        for (Index i : targetIndex) {
            FinancialRecord fr = model.getFilteredFinancialRecordList().get(i.getZeroBased());
            frList.add(fr.getDescription());
        }

        model.updateFilteredFinancialRecordList(record -> frList.contains(record.getDescription()));

        assertEquals(targetIndex.size(), model.getFilteredFinancialRecordList().size());
    }

}


