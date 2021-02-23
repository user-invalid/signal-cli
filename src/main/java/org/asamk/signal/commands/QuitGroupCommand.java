package org.asamk.signal.commands;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import org.asamk.signal.PlainTextWriterImpl;
import org.asamk.signal.manager.Manager;
import org.asamk.signal.manager.groups.GroupIdFormatException;
import org.asamk.signal.manager.groups.GroupNotFoundException;
import org.asamk.signal.manager.groups.NotAGroupMemberException;
import org.asamk.signal.util.Util;

import java.io.IOException;

import static org.asamk.signal.util.ErrorUtils.handleAssertionError;
import static org.asamk.signal.util.ErrorUtils.handleGroupIdFormatException;
import static org.asamk.signal.util.ErrorUtils.handleGroupNotFoundException;
import static org.asamk.signal.util.ErrorUtils.handleIOException;
import static org.asamk.signal.util.ErrorUtils.handleNotAGroupMemberException;
import static org.asamk.signal.util.ErrorUtils.handleTimestampAndSendMessageResults;

public class QuitGroupCommand implements LocalCommand {

    @Override
    public void attachToSubparser(final Subparser subparser) {
        subparser.addArgument("-g", "--group").required(true).help("Specify the recipient group ID.");
    }

    @Override
    public int handleCommand(final Namespace ns, final Manager m) {
        try {
            final var writer = new PlainTextWriterImpl(System.out);

            final var groupId = Util.decodeGroupId(ns.getString("group"));
            final var results = m.sendQuitGroupMessage(groupId);
            return handleTimestampAndSendMessageResults(writer, results.first(), results.second());
        } catch (IOException e) {
            handleIOException(e);
            return 3;
        } catch (AssertionError e) {
            handleAssertionError(e);
            return 1;
        } catch (GroupNotFoundException e) {
            handleGroupNotFoundException(e);
            return 1;
        } catch (NotAGroupMemberException e) {
            handleNotAGroupMemberException(e);
            return 1;
        } catch (GroupIdFormatException e) {
            handleGroupIdFormatException(e);
            return 1;
        }
    }
}
