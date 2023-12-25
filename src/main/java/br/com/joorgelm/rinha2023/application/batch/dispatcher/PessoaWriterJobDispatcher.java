package br.com.joorgelm.rinha2023.application.batch.dispatcher;

import br.com.joorgelm.rinha2023.application.batch.launcher.PessoaWriterJobLauncher;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PessoaWriterJobDispatcher {

    private final PessoaWriterJobLauncher pessoaWriterJobLauncher;

    private final Job WriterJob;

    public PessoaWriterJobDispatcher(PessoaWriterJobLauncher pessoaWriterJobLauncher, Job writerJob) {
        this.pessoaWriterJobLauncher = pessoaWriterJobLauncher;
        WriterJob = writerJob;
    }

    public void run() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        System.out.println("PessoaWriterJobDispatcher.run");

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("moment", LocalDateTime.now())
                .toJobParameters();

        pessoaWriterJobLauncher.run(WriterJob, jobParameters);
    }
}
